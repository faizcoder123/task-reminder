package com.taskreminder.controller;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.responsedto.UserResponse;
import com.taskreminder.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;


@RequestMapping("/taskReminder")
@CrossOrigin(
        allowCredentials = "true",
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registerUser")
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
    public ResponseEntity<String> deleteUser(@PathVariable long id, Principal principal) throws ApiRequestException, IOException {
        userService.deleteUser(id, principal);
        return new ResponseEntity<>("{DELETED}", HttpStatus.OK);
    }

}
