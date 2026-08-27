package com.smartjobportal.controller;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.model.Job;
import com.smartjobportal.model.Skill;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private DataService dataService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        List<Job> allJobs = dataService.getAllJobs();
        int total = allJobs.size();
        int start = (page - 1) * limit;
        List<Job> paginated = allJobs.stream()
                .skip(start)
                .limit(limit)
                .toList();
        Map<String, Object> result = Map.of(
                "jobs", paginated,
                "total", total,
                "page", page,
                "limit", limit
        );
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchJobs(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String job_type,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "relevance") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        List<Job> filtered = dataService.searchJobs(q, location, experience, job_type, salary, company);

        for (Job job : filtered) {
            List<Skill> jobSkills = dataService.getSkillsForJob(job.getId());
            List<String> skillNames = jobSkills.stream()
                    .map(Skill::getSkillName)
                    .toList();
            job.setTitle(job.getTitle() + " [" + String.join(", ", skillNames) + "]");
        }

        if ("salary".equalsIgnoreCase(sort)) {
            filtered.sort((a, b) -> extractFirstNumber(b.getSalary()) - extractFirstNumber(a.getSalary()));
        } else if ("experience".equalsIgnoreCase(sort)) {
            filtered.sort((a, b) -> extractFirstNumber(b.getExperience()) - extractFirstNumber(a.getExperience()));
        }

        int total = filtered.size();
        int start = (page - 1) * limit;
        List<Job> paginated = filtered.stream()
                .skip(start)
                .limit(limit)
                .toList();

        Map<String, Object> result = Map.of(
                "jobs", paginated,
                "total", total,
                "page", page,
                "limit", limit
        );
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Optional<Job>>> getJob(@PathVariable Long id) {
        Optional<Job> job = dataService.getJobById(id);
        if (job.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(job));
        }
        return ResponseEntity.ok(ApiResponse.error("Job not found"));
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getJobSkills(@PathVariable Long id) {
        List<Skill> skills = dataService.getSkillsForJob(id);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<ApiResponse<List<Job>>> getSimilarJobs(@PathVariable Long id) {
        List<Job> similar = dataService.getSimilarJobs(id);
        return ResponseEntity.ok(ApiResponse.success(similar));
    }

    @PostMapping("/{userId}/save")
    public ResponseEntity<ApiResponse<String>> saveJob(
            @PathVariable Long userId,
            @RequestParam Long jobId) {
        dataService.saveJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job saved successfully"));
    }

    @DeleteMapping("/{userId}/unsave/{jobId}")
    public ResponseEntity<ApiResponse<String>> unsaveJob(
            @PathVariable Long userId,
            @PathVariable Long jobId) {
        dataService.unsaveJob(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job unsaved successfully"));
    }

    @GetMapping("/user/{userId}/saved")
    public ResponseEntity<ApiResponse<List<Job>>> getSavedJobs(@PathVariable Long userId) {
        List<com.smartjobportal.model.SavedJob> saved = dataService.getSavedJobsByUser(userId);
        List<Job> jobs = new ArrayList<>();
        for (com.smartjobportal.model.SavedJob sj : saved) {
            dataService.getJobById(sj.getJobId()).ifPresent(jobs::add);
        }
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    private int extractFirstNumber(String text) {
        if (text == null) return 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(text);
        return m.find() ? Integer.parseInt(m.group()) : 0;
    }
}
