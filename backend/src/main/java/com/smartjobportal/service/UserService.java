package com.smartjobportal.service;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.entity.User;
import com.smartjobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<User> createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ApiResponse.error("Email already exists");
        }
        User saved = userRepository.save(user);
        return ApiResponse.success("User created successfully", saved);
    }

    public ApiResponse<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ApiResponse.success(value))
                .orElseGet(() -> ApiResponse.error("User not found"));
    }

    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ApiResponse.success(users);
    }
}
