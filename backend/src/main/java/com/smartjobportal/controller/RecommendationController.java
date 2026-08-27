package com.smartjobportal.controller;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.model.Job;
import com.smartjobportal.model.Skill;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private DataService dataService;

    @GetMapping("/recommendations")
    public ApiResponse<List<Job>> getRecommendations(@RequestParam Long user_id) {
        List<Job> allJobs = dataService.getAllJobs();
        List<Job> recommended = allJobs.stream()
                .limit(20)
                .collect(Collectors.toList());
        return ApiResponse.success(recommended);
    }
}
