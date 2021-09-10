package com.taskreminder.controller;

import com.taskreminder.entities.UserEntity;
import com.taskreminder.handler.ApiRequestException;
import com.taskreminder.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/taskReminder")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/addUser")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserEntity user) throws ApiRequestException{
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable long id) throws ApiRequestException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserEntity> getUserByMail(@RequestParam(required = true) String mail) throws ApiRequestException{
        return new ResponseEntity<>(userService.getUserByGmail(mail), HttpStatus.OK);
    }

}
