package com.winners.lostbutfound.services.implementation;

import com.winners.lostbutfound.dtos.*;
import com.winners.lostbutfound.exceptions.ItemNotFoundException;
import com.winners.lostbutfound.exceptions.RegistrationException;
import com.winners.lostbutfound.models.Item;
import com.winners.lostbutfound.repositories.ItemRepository;
import com.winners.lostbutfound.services.ItemOwnerService;
import com.winners.lostbutfound.services.ItemService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.winners.lostbutfound.models.ItemStatus.CLAIMED;
import static com.winners.lostbutfound.models.ItemStatus.NOT_CLAIMED;
import static com.winners.lostbutfound.utils.UserValidator.*;
import static com.winners.lostbutfound.utils.UserValidator.ValidationResult.SUCCESS;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final FirebaseService firebaseService;
    private final MongoTemplate mongoTemplate;

    public ItemServiceImpl(ItemRepository itemRepository, FirebaseService firebaseService, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.firebaseService = firebaseService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Override
    public RegistrationResponse registerItem(
            @Valid ItemRegistrationRequest itemRegistrationRequest,
            MultipartFile imageFile) throws IOException {

        validateEmailAndPhoneNumber(itemRegistrationRequest.finderEmail(),
                itemRegistrationRequest.finderPhoneNumber());

        int highestItemNumber = itemRepository.findTopByOrderByItemNumberDesc()
                .map(Item::getItemNumber)
                .orElse(0);

        String imageUrl = firebaseService.uploadFile(imageFile);

        Item item = Item.builder()
                .name(itemRegistrationRequest.name())
                .description(itemRegistrationRequest.description())
                .locationFound(itemRegistrationRequest.locationFound())
                .dateFound(LocalDate.now())
                .finderName(itemRegistrationRequest.finderName())
                .finderEmail(itemRegistrationRequest.finderEmail())
                .finderPhoneNumber(itemRegistrationRequest.finderPhoneNumber())
                .itemNumber(highestItemNumber + 1)
                .image(imageUrl)
                .build();

        itemRepository.save(item);

        return RegistrationResponse.builder()
                .message("Item saved successfully")
                .build();
    }

    private static void validateEmailAndPhoneNumber(String email, String phoneNumber) {
        ValidationResult isValidEmail = isValidEmail1().apply(email);
        ValidationResult isValidPhoneNumber =  isValidPhoneNumber().apply(phoneNumber);

        if (isValidEmail != SUCCESS)
            throw  new RegistrationException("Incorrect email format!");
        if (isValidPhoneNumber != SUCCESS)
            throw new RegistrationException("Incorrect phone number format!");
    }

    @Override
    public ClaimItemResponse claimItem(@Valid ClaimRequest claimRequest, MultipartFile imageFile) throws IOException {
        Item item = findByItemNumber(Integer.valueOf(claimRequest.itemNo()));

        if (item.getItemStatus() != CLAIMED) {

            validateEmailAndPhoneNumber(claimRequest.ownerEmail(), claimRequest.ownerPhoneNumber());
            String imageUrl = firebaseService.uploadFile(imageFile);

            item.setItemStatus(CLAIMED);
            item.setClaimDate(LocalDate.now());
            item.setOwnerFirstName(claimRequest.ownerFirstName());
            item.setOwnerLastName(claimRequest.ownerLastName());
            item.setOwnerEmail(claimRequest.ownerEmail());
            item.setOwnerPhoneNumber(claimRequest.ownerPhoneNumber());
            item.setOwnerImage(imageUrl);
            item.setAdminName(claimRequest.adminName());
            save(item);

            return new ClaimItemResponse("Item claimed successfully");
        }else {
            return new ClaimItemResponse("Item is already claimed");
        }
    }
    @Override
    public List<Item> findByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name).orElseThrow(
                ()-> new ItemNotFoundException("Items with name "+ name + " not found"));
    }
    @Override
    public List<Item> findByDescription(String description) {
        return itemRepository.findByDescriptionContainingIgnoreCase(description).orElseThrow(
                ()-> new ItemNotFoundException("Items with name "+ description +" not found"));
    }

    @Override
    public List<Item> findAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Page<Item> findAllItem(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public List<Item> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return mongoTemplate.find(
                Query.query(Criteria.where("claimDate").gte(startDate).lte(endDate)),
                Item.class);
    }
    @Override
    public List<ItemDTO> searchItem(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            throw new IllegalStateException("search term cannot be null or empty");
        }

        String[] keywords = searchQuery.split("\\s+");
        Set<Item> resultSet = new HashSet<>();

        for (String keyword : keywords) {
            List<Item> itemsByDescription = findByDescription(keyword);

            if (resultSet.isEmpty()) {
                resultSet.addAll(itemsByDescription);
            } else {
                resultSet.retainAll(itemsByDescription);
            }

            if (resultSet.isEmpty()) {
                break;
            }
        }

        return resultSet.stream()
                .map(item -> new ItemDTO(
                        item.getName(),
                        item.getItemNumber(),
                        item.getDescription(),
                        item.getItemStatus().toString(),
                        item.getDateFound(),
                        item.getFinderName(),
                        item.getFinderEmail(),
                        item.getFinderPhoneNumber(),
                        item.getImage()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Item findByItemNumber(Integer itemNumber) {
        return itemRepository.findByItemNumber(itemNumber).orElseThrow(
                () -> new ItemNotFoundException("Item number " + itemNumber + " not found"));
    }

    @Override
    public long countByDateBetween(LocalDate startDate, LocalDate endDate) {
        return mongoTemplate.find(
                Query.query(Criteria.where("dateFound").gte(startDate).lte(endDate)),
                Item.class).size();
    }

    @Override
    public long countByDateBetweenForClaimedItems(LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("itemStatus").is("CLAIMED")
                        .and("claimDate").gte(startDate).lte(endDate));

        return mongoTemplate.find(query, Item.class).size();
    }

    @Override
    public long countByDateBetweenForUnclaimedItems(LocalDate startDate, LocalDate endDate) {
        return mongoTemplate.find(Query.query(Criteria.where("dateFound").gte(startDate).lte(endDate)), Item.class).stream()
                .filter(item -> item.getItemStatus().equals(NOT_CLAIMED))
                .count();
    }
    @Override
    public String unclaimedItemSummary(long count) {
        return "There are " + count + " item(s) still unclaimed within the period.";
    }
    @Override
    public Map<LocalDate, Long> countClaimsByDate(LocalDate startDate, LocalDate endDate) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("claimDate").gte(startDate).lte(endDate));

        GroupOperation groupOperation = Aggregation.group(
                "claimDate").count().as("count");

        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("_id").as("claimDate")
                .andExpression("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupOperation, projectionOperation);

        List<ClaimDateCount> result = mongoTemplate.aggregate(
                aggregation, "item", ClaimDateCount.class).getMappedResults();

        return result.stream().collect(
                Collectors.toMap(ClaimDateCount::getClaimDate, ClaimDateCount::getCount));
    }

    @Override
    public Map<YearMonth, Long> countClaimsByMonth(LocalDate startDate, LocalDate endDate) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("claimDate").gte(startDate).lte(endDate));


        GroupOperation groupOperation = Aggregation.group(
                Aggregation.fields()
                        .and("year", "claimDate").and("month", "claimDate")
        ).count().as("count");

        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("year").as("year")
                .andExpression("month").as("month")
                .andExpression("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);

        List<ClaimMonthCount> result = mongoTemplate.aggregate(
                aggregation, "item", ClaimMonthCount.class).getMappedResults();

        return result.stream()
                .filter(item -> item.getMonth() >= 1 && item.getMonth() <= 12)
                .collect(Collectors.toMap(
                        item -> YearMonth.of(item.getYear(), item.getMonth()),
                        ClaimMonthCount::getCount
                ));
    }

    @Getter
    @Setter
    private static class ClaimDateCount {
        private LocalDate claimDate;
        private long count;

    }

    @Getter
    @Setter
    private static class ClaimMonthCount {
        private int year;
        private int month;
        private long count;

    }

}
