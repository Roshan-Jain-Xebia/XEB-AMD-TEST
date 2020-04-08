package com.uxpsystems.assignment.controller;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        LOGGER.info("Get all users");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id){
        LOGGER.info("Get user for {}", id);
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        LOGGER.info("Create user for {}", user);
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable long id){
        LOGGER.info("Delete user for {}", id);
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User updateUser(@RequestBody User user, @PathVariable long id){
        user.setId(id);
        LOGGER.info("Update user for {}", user);
        return userService.updateUser(user);
    }
}

