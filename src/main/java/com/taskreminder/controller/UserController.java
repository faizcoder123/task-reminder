package com.taskreminder.controller;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequestMapping("/taskReminder")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/addUser")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity user) throws ApiRequestException{
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @PatchMapping(value = "/updateUser/{id}")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user, @PathVariable long id, Principal principal) throws ApiRequestException{
        return new ResponseEntity<>(userService.updateUser(user, id, principal), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(userService.getUser(id,principal), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserEntity> getUserByMail(@RequestParam(required = true) String mail, Principal principal) throws ApiRequestException{
        return new ResponseEntity<>(userService.getUserByGmail(mail, principal.getName()), HttpStatus.OK);
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserEntity>> getAllUsers() throws ApiRequestException {
//        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
//    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable long id, Principal principal) throws ApiRequestException {
        return new ResponseEntity<>(userService.deleteUser(id, principal), HttpStatus.OK);
    }

}
