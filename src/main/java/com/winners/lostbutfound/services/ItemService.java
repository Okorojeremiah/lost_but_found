package com.winners.lostbutfound.services;

import com.winners.lostbutfound.dtos.*;
import com.winners.lostbutfound.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface ItemService {

    void save(Item item);
    RegistrationResponse registerItem(ItemRegistrationRequest itemRegistrationRequest,
                                      MultipartFile imageFile) throws IOException;
    ClaimItemResponse claimItem(ClaimRequest claimRequest, MultipartFile imageUrl) throws IOException;
    Item findByItemNumber(Integer itemNumber);
    List<Item> findByName(String name);
    List<ItemDTO> searchItem(String searchTerm);
    List<Item> findByDescription(String description);

    List<Item> findByDateRange(LocalDate startDate, LocalDate endDate);
    long countByDateBetween(LocalDate startDate, LocalDate endDate);
    long countByDateBetweenForClaimedItems(LocalDate startDate, LocalDate endDate);
    Map<LocalDate, Long> countClaimsByDate(LocalDate startDate, LocalDate endDate);
    Map<YearMonth, Long> countClaimsByMonth(LocalDate startDate, LocalDate endDate);
    String unclaimedItemSummary(long count);
    long countByDateBetweenForUnclaimedItems(LocalDate startDate, LocalDate endDate);
    List<Item> findAllItem();

    Page<Item> findAllItem(Pageable pageable);
}
