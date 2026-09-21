package com.smartjobportal.controller;

import com.smartjobportal.model.Job;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private DataService dataService;

    @GetMapping("/recommendations")
    public Map<String, Object> getRecommendations(@RequestParam Long user_id) {
        List<Job> allJobs = dataService.getAllJobs();
        List<Job> recommended = allJobs.stream()
                .limit(20)
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("recommendations", recommended);
        return result;
    }
}
