package com.winners.lostbutfound.services;

import com.winners.lostbutfound.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    User findById(String userId);
    List<User> findAllUsers();
}
