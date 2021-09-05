package com.taskreminder.services;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUser() {
        return new ArrayList<UserEntity>(userRepository.findAll());
    }

    public UserEntity addNewUser(UserEntity user) {
        userRepository.save(user);
        return user;
    }

//    public UserEntity updateUser() {
//        return
//    }

    public UserEntity getUser(UUID id) {
        return userRepository.findById(id).get();
    }

    public UserEntity getUserByGmail(String gmail) {
        return userRepository.findByEmail(gmail).get();
    }

}
