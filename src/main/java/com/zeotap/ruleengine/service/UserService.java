package com.zeotap.ruleengine.service;

import com.zeotap.ruleengine.exception.UserNotFoundException;
import com.zeotap.ruleengine.model.User;
import com.zeotap.ruleengine.model.UserDto;
import com.zeotap.ruleengine.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    // Method to add a new user
    public UserDto addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class); // This should now include id
    }

    // Method to get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class)) // This should now include id
                .collect(Collectors.toList());
    }


    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class)); // Convert User to UserDto
    }

    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            // Map the incoming userDto to the existing user
            modelMapper.map(userDto, existingUser);
            // No need to set the id again, since existingUser already has the id
            User updatedUser = userRepository.save(existingUser);
            return modelMapper.map(updatedUser, UserDto.class);
        });
    }


    public Optional<UserDto> updateUserByName(String name, UserDto userDto) {
        List<User> users = userRepository.findByName(name);
        if (users.isEmpty()) {
            return Optional.empty(); // No users found with the given name
        }

        // Update each user with the provided userDto information
        List<UserDto> updatedUsers = new ArrayList<>();
        for (User user : users) {
            modelMapper.map(userDto, user); // Map fields from userDto to the existing user
            updatedUsers.add(modelMapper.map(userRepository.save(user), UserDto.class)); // Save and convert to UserDto
        }

        return Optional.of(updatedUsers.get(0)); // Return the first updated user as Optional
    }


    // Method to delete a user by ID
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        throw new UserNotFoundException(id);
    }

    // Method to find users by name
    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}

