package com.smartjobportal.controller;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.model.User;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private DataService dataService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        long newId = dataService.getAllUsers().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;
        user.setId(newId);
        user.setCreatedAt(java.time.LocalDateTime.now());
        dataService.getAllUsers().add(user);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<User>>> getUser(@PathVariable Long id) {
        User user = dataService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(ApiResponse.success(Optional.of(user)));
        }
        return ResponseEntity.ok(ApiResponse.error("User not found"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = dataService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
