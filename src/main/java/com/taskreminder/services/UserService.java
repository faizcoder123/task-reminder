package com.taskreminder.services;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUser() {
        List<UserEntity> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new ApiRequestException("No Users found");
        }
        return users;
    }

    public UserEntity deleteUser(long id) {
        UserEntity user = getUser(id);
        userRepository.deleteById(id);
        return user;
    }

    public UserEntity saveUser(UserEntity user) {
        userRepository.save(user);
        return user;
    }

    public UserEntity updateUser(UserEntity user, long id) {
        getUser(id);
        user.setOwnerId(id);
        return saveUser(user);
    }

    public UserEntity getUser(long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new ApiRequestException("User not found");
        }
    }

    public UserEntity getUserByGmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new ApiRequestException("User not found with this Email");
        }
    }
}
