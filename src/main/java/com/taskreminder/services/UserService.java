package com.taskreminder.services;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.UserRepository;
import com.taskreminder.responsedto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse deleteUser(long id, Principal principal) {
        UserEntity user = getUserById(id, principal);
        userRepository.deleteById(id);
        return new UserResponse(user.getOwnerId(), user.getUserName());
    }

    public UserResponse saveUser(UserEntity user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return new UserResponse(user.getOwnerId(), user.getUserName());
    }

    public UserResponse updateUser(UserEntity user, long id, Principal principal) {
        getUserById(id, principal);
        user.setOwnerId(id);
        return new UserResponse(user.getOwnerId(), user.getUserName());
    }

    public UserEntity getUserById(long id, Principal principal) {
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            if(!principal.getName().equals(user.get().getEmail())) throw new ApiRequestException("Authentication failed");
            return user.get();
        }
        else {
            throw new ApiRequestException("User not found");
        }
    }
    public UserResponse getUserByGmail(String email, String principalMail) {
        if(!email.equals(principalMail))  throw new ApiRequestException("Authentication failed");
        UserEntity user = getUser(email);
        if(user != null){
            return new UserResponse(user.getOwnerId(), user.getUserName());
        }
        else {
            throw new ApiRequestException("User not found with this Email");
        }
    }

    public UserEntity getUser(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
}
