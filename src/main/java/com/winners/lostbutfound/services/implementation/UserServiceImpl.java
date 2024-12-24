package com.winners.lostbutfound.services.implementation;


import com.winners.lostbutfound.models.User;
import com.winners.lostbutfound.repositories.UserRepo;
import com.winners.lostbutfound.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;


    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    @Override
    public User findById(String userId){
        return userRepository.findById(userId).orElseThrow(
                ()-> new UsernameNotFoundException("user with id " + userId + " not found"));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


}
