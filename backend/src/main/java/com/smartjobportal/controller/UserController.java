package com.smartjobportal.controller;

import com.smartjobportal.model.User;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private DataService dataService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        User created = dataService.addUser(user);
        Map<String, Object> result = new HashMap<>();
        result.put("id", created.getId());
        result.put("name", created.getName());
        result.put("email", created.getEmail());
        result.put("location", created.getLocation());
        result.put("experience", created.getExperience());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        User user = dataService.getUserById(id);
        if (user != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("location", user.getLocation());
            userMap.put("experience", user.getExperience());
            return ResponseEntity.ok(userMap);
        }
        return ResponseEntity.status(404).body(Map.of("error", "User not found"));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = dataService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
