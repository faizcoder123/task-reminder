package com.taskreminder.controller;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.responsedto.UserResponse;
import com.taskreminder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RequestMapping("/taskReminder")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/addUser")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserEntity user) throws ApiRequestException{
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @PatchMapping(value = "/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserEntity user, Principal principal) throws ApiRequestException{
        return new ResponseEntity<>(userService.updateUser(user, principal), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUserByMail(Principal principal) throws ApiRequestException{
        return new ResponseEntity<>(userService.getUserByGmail(principal.getName()), HttpStatus.OK);
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserEntity>> getAllUsers() throws ApiRequestException {
//        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
//    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(userService.deleteUser(id, principal), HttpStatus.OK);
    }

}
