package com.smartjobportal.service;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.JobSkill;
import com.smartjobportal.entity.SavedJob;
import com.smartjobportal.entity.Skill;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.JobSkillRepository;
import com.smartjobportal.repository.SavedJobRepository;
import com.smartjobportal.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobSkillRepository jobSkillRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    public ApiResponse<Map<String, Object>> getAllJobs(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Job> jobPage = jobRepository.findAll(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("jobs", jobPage.getContent());
        result.put("total", jobPage.getTotalElements());
        result.put("page", page);
        result.put("limit", limit);
        return ApiResponse.success(result);
    }

    public ApiResponse<Optional<Job>> getJobById(Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            return ApiResponse.success(Optional.of(job.get()));
        }
        return ApiResponse.error("Job not found");
    }

    public ApiResponse<List<Skill>> getJobSkills(Long jobId) {
        List<JobSkill> jobSkills = jobSkillRepository.findByJobId(jobId);
        List<Skill> skills = jobSkills.stream()
                .map(JobSkill::getSkill)
                .toList();
        return ApiResponse.success(skills);
    }

    public ApiResponse<List<Job>> getSimilarJobs(Long jobId) {
        Optional<Job> currentJob = jobRepository.findById(jobId);
        if (currentJob.isEmpty()) {
            return ApiResponse.error("Job not found");
        }
        List<Job> allJobs = jobRepository.findAll();
        List<Job> similar = allJobs.stream()
                .filter(j -> !j.getId().equals(jobId))
                .limit(4)
                .toList();
        return ApiResponse.success(similar);
    }

    public ApiResponse<String> saveJob(Long userId, Long jobId) {
        Optional<SavedJob> existing = savedJobRepository.findByUserIdAndJobId(userId, jobId);
        if (existing.isPresent()) {
            return ApiResponse.error("Job already saved");
        }
        SavedJob savedJob = new SavedJob();
        savedJob.setUserId(userId);
        savedJob.setJobId(jobId);
        savedJobRepository.save(savedJob);
        return ApiResponse.success("Job saved successfully");
    }

    public ApiResponse<String> unsaveJob(Long userId, Long jobId) {
        savedJobRepository.deleteByUserIdAndJobId(userId, jobId);
        return ApiResponse.success("Job unsaved successfully");
    }

    public ApiResponse<List<Job>> getSavedJobs(Long userId) {
        List<SavedJob> savedJobs = savedJobRepository.findByUserId(userId);
        List<Job> jobs = savedJobs.stream()
                .map(sj -> jobRepository.findById(sj.getJobId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
        return ApiResponse.success(jobs);
    }
}
