package com.winners.lostbutfound.repositories;

import com.winners.lostbutfound.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByEmail(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
