package com.winners.lostbutfound.repositories;

import com.google.common.io.Files;
import com.winners.lostbutfound.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ItemRepository extends MongoRepository<Item, String> {

    Optional<Item> findByItemNumber(Integer itemNumber);
    Optional<List<Item>> findByNameContainingIgnoreCase(String name);
    Optional<List<Item>> findByDescriptionContainingIgnoreCase(String description);
    Optional<Item> findTopByOrderByItemNumberDesc();

}
