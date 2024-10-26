package com.zeotap.ruleengine.controller;

import com.zeotap.ruleengine.model.UserDto;
import com.zeotap.ruleengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.addUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.findById(id);
        return userDto.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Endpoint to get users by name
    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserDto>> getUserByName(@PathVariable String name) {
        List<UserDto> users = userService.findByName(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint to update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto)
                .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to update users by name
    @PutMapping("/name/{name}")
    public ResponseEntity<UserDto> updateUserByName(@PathVariable String name, @RequestBody UserDto userDto) {
        Optional<UserDto> updatedUser = userService.updateUserByName(name, userDto);
        return updatedUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
