package com.smartjobportal.controller;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.dto.LoginRequest;
import com.smartjobportal.model.User;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private DataService dataService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        User user = dataService.authenticate(request.getEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.error("Invalid email or password"));
        }
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", user.getId());
        responseData.put("name", user.getName());
        responseData.put("email", user.getEmail());
        responseData.put("location", user.getLocation());
        responseData.put("experience", user.getExperience());
        return ResponseEntity.ok(ApiResponse.success("Login successful", responseData));
    }
}
