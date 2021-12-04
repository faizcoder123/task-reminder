package com.taskreminder.services;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.repository.UserRepository;
import com.taskreminder.responsedto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  TaskService taskService;

    public List<UserEntity> getAllUsers() throws ApiRequestException{
        return userRepository.findAll();
    }

    public void deleteUser(long id, Principal principal) throws ApiRequestException, IOException {
        getUserById(id);
        userRepository.deleteById(id);
        taskService.deleteAllTaskOfUser(principal.getName());
    }

    public UserResponse saveUser(UserEntity user) throws ApiRequestException{
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return new UserResponse(user.getOwnerId(), user.getUserName(), user.getEmail(), user.getPhoneNo());
    }

    public UserResponse updateUser(UserEntity user, Principal principal) throws ApiRequestException{
        getUserById(user.getOwnerId());
        saveUser(user);
        return new UserResponse(user.getOwnerId(), user.getUserName(), user.getEmail(), user.getPhoneNo());
    }

    public UserEntity getUserById(long id) throws ApiRequestException{
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new ApiRequestException("User not found");
        }
    }
    public UserResponse getUserByGmail(String email) throws ApiRequestException{
        UserEntity user = getUser(email);
        return new UserResponse(user.getOwnerId(), user.getUserName(), user.getEmail(), user.getPhoneNo());
    }

    public UserEntity getUser(String email) throws ApiRequestException{
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new ApiRequestException("User not found with this Email");
    }
}
