package com.taskreminder.services;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

    public UserEntity deleteUser(long id, Principal principal) {

        UserEntity user = getUser(id, principal);
        userRepository.deleteById(id);
        SecurityContextHolder.clearContext();
        return user;
    }

    public UserEntity saveUser(UserEntity user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        SecurityContextHolder.clearContext();
        return user;
    }

    public UserEntity updateUser(UserEntity user, long id, Principal principal) {
        getUser(id, principal);
        user.setOwnerId(id);
        SecurityContextHolder.clearContext();
        return saveUser(user);
    }

    public UserEntity getUser(long id, Principal principal) {
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            if(!principal.getName().equals(user.get().getEmail())) throw new ApiRequestException("Authentication failed");
            SecurityContextHolder.clearContext();
            return user.get();
        }
        else {
            SecurityContextHolder.clearContext();
            throw new ApiRequestException("User not found");
        }
    }

    public UserEntity getUserByGmail(String email, String userMail) {
        if(!email.equals(userMail))  throw new ApiRequestException("Authentication failed");

        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            SecurityContextHolder.clearContext();
            return user.get();
        }
        else {
            SecurityContextHolder.clearContext();
            throw new ApiRequestException("User not found with this Email");
        }
    }
}
