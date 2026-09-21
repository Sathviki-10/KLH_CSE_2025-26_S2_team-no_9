package com.smartjobportal.controller;

import com.smartjobportal.model.Job;
import com.smartjobportal.model.Skill;
import com.smartjobportal.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private DataService dataService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        List<Job> allJobs = dataService.getAllJobs();
        int total = allJobs.size();
        int start = (page - 1) * limit;
        List<Job> paginated = allJobs.stream()
                .skip(start)
                .limit(limit)
                .toList();
        Map<String, Object> result = new HashMap<>();
        result.put("jobs", paginated);
        result.put("total", total);
        result.put("page", page);
        result.put("limit", limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchJobs(
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

        List<Job> filtered = dataService.searchJobs(q, skills, location, experience, job_type, salary, company);

        boolean isFallback = false;
        String fallbackReason = null;
        if (filtered.isEmpty() && (q != null && !q.isBlank())) {
            isFallback = true;
            fallbackReason = "No exact matches found. Showing related jobs.";
            filtered = getRelatedJobs(q, location, skills, job_type);
        } else if (filtered.isEmpty() && !dataService.getAllJobs().isEmpty()) {
            isFallback = true;
            fallbackReason = "No exact matches found. Showing the latest available jobs.";
            filtered = dataService.getAllJobs().stream()
                    .limit(20)
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> responseJobs = new ArrayList<>();
        for (Job job : filtered) {
            List<Skill> jobSkills = dataService.getSkillsForJob(job.getId());
            List<String> skillNames = jobSkills.stream()
                    .map(Skill::getSkillName)
                    .toList();
            Map<String, Object> jobMap = new HashMap<>();
            jobMap.put("id", job.getId());
            jobMap.put("title", job.getTitle());
            jobMap.put("company", job.getCompany());
            jobMap.put("location", job.getLocation());
            jobMap.put("description", job.getDescription());
            jobMap.put("experience", job.getExperience());
            jobMap.put("salary", job.getSalary());
            jobMap.put("job_type", job.getJobType());
            jobMap.put("created_at", job.getCreatedAt());
            jobMap.put("skills", skillNames);
            jobMap.put("relevance_score", isFallback ? 0 : 0);
            jobMap.put("is_fallback", isFallback);
            responseJobs.add(jobMap);
        }

        if ("salary".equalsIgnoreCase(sort)) {
            responseJobs.sort((a, b) -> extractFirstNumber((String) b.get("salary")) - extractFirstNumber((String) a.get("salary")));
        } else if ("experience".equalsIgnoreCase(sort)) {
            responseJobs.sort((a, b) -> extractFirstNumber((String) b.get("experience")) - extractFirstNumber((String) a.get("experience")));
        }

        int total = responseJobs.size();
        int start = (page - 1) * limit;
        List<Map<String, Object>> paginated = responseJobs.stream()
                .skip(start)
                .limit(limit)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("jobs", paginated);
        result.put("total", total);
        result.put("page", page);
        result.put("limit", limit);
        result.put("is_fallback", isFallback);
        if (isFallback && fallbackReason != null) {
            result.put("message", fallbackReason);
        }
        return ResponseEntity.ok(result);
    }

    private List<Job> getRelatedJobs(String query, String location, String skills, String jobType) {
        List<Job> allJobs = dataService.getAllJobs();
        String q = query.toLowerCase();
        return allJobs.stream()
                .sorted((a, b) -> {
                    int scoreA = computeRelatedScore(a, q, location, skills, jobType);
                    int scoreB = computeRelatedScore(b, q, location, skills, jobType);
                    return Integer.compare(scoreB, scoreA);
                })
                .limit(20)
                .collect(Collectors.toList());
    }

    private int computeRelatedScore(Job job, String query, String location, String skills, String jobType) {
        int score = 0;
        String title = job.getTitle().toLowerCase();
        String company = job.getCompany().toLowerCase();
        String description = job.getDescription().toLowerCase();
        String jobLoc = job.getLocation().toLowerCase();
        String[] queryWords = query.toLowerCase().split("\\s+");
        for (String word : queryWords) {
            if (word.length() < 2) continue;
            if (title.contains(word)) score += 10;
            if (company.contains(word)) score += 5;
            if (description.contains(word)) score += 2;
        }
        if (location != null && !location.isBlank() && jobLoc.contains(location.toLowerCase())) {
            score += 15;
        }
        if (skills != null && !skills.isBlank()) {
            List<Skill> jobSkills = dataService.getSkillsForJob(job.getId());
            List<String> skillNames = jobSkills.stream()
                    .map(Skill::getSkillName)
                    .map(String::toLowerCase)
                    .toList();
            String[] skillList = skills.toLowerCase().split(",");
            for (String skill : skillList) {
                skill = skill.trim();
                if (skill.isEmpty()) continue;
                for (String jobSkill : skillNames) {
                    if (jobSkill.contains(skill) || skill.contains(jobSkill)) {
                        score += 8;
                        break;
                    }
                }
            }
        }
        if (jobType != null && !jobType.isBlank() && job.getJobType().toLowerCase().contains(jobType.toLowerCase())) {
            score += 5;
        }
        return score;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getJob(@PathVariable Long id) {
        Optional<Job> jobOpt = dataService.getJobById(id);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            Map<String, Object> jobMap = new HashMap<>();
            jobMap.put("id", job.getId());
            jobMap.put("title", job.getTitle());
            jobMap.put("company", job.getCompany());
            jobMap.put("location", job.getLocation());
            jobMap.put("description", job.getDescription());
            jobMap.put("experience", job.getExperience());
            jobMap.put("salary", job.getSalary());
            jobMap.put("job_type", job.getJobType());
            jobMap.put("created_at", job.getCreatedAt());
            List<Skill> skills = dataService.getSkillsForJob(job.getId());
            jobMap.put("skills", skills.stream().map(Skill::getSkillName).toList());
            return ResponseEntity.ok(jobMap);
        }
        return ResponseEntity.status(404).body(Map.of("error", "Job not found"));
    }

    @GetMapping("/{id}/skills")
    public ResponseEntity<List<Skill>> getJobSkills(@PathVariable Long id) {
        List<Skill> skills = dataService.getSkillsForJob(id);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Job>> getSimilarJobs(@PathVariable Long id) {
        List<Job> similar = dataService.getSimilarJobs(id);
        return ResponseEntity.ok(similar);
    }

    @PostMapping("/{userId}/save")
    public ResponseEntity<Map<String, String>> saveJob(
            @PathVariable Long userId,
            @RequestParam Long jobId) {
        dataService.saveJob(userId, jobId);
        return ResponseEntity.ok(Map.of("message", "Job saved successfully"));
    }

    @DeleteMapping("/{userId}/unsave/{jobId}")
    public ResponseEntity<Map<String, String>> unsaveJob(
            @PathVariable Long userId,
            @PathVariable Long jobId) {
        dataService.unsaveJob(userId, jobId);
        return ResponseEntity.ok(Map.of("message", "Job unsaved successfully"));
    }

    @GetMapping("/user/{userId}/saved")
    public ResponseEntity<Map<String, Object>> getSavedJobs(@PathVariable Long userId) {
        List<com.smartjobportal.model.SavedJob> saved = dataService.getSavedJobsByUser(userId);
        List<Job> jobs = new ArrayList<>();
        for (com.smartjobportal.model.SavedJob sj : saved) {
            dataService.getJobById(sj.getJobId()).ifPresent(jobs::add);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("saved_jobs", jobs);
        return ResponseEntity.ok(result);
    }

    private int extractFirstNumber(String text) {
        if (text == null) return 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(text);
        return m.find() ? Integer.parseInt(m.group()) : 0;
    }
}
