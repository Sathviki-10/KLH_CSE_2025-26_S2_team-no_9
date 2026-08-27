package com.smartjobportal.service;

import com.smartjobportal.dto.ApiResponse;
import com.smartjobportal.entity.Job;
import com.smartjobportal.entity.Skill;
import com.smartjobportal.repository.JobRepository;
import com.smartjobportal.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    private static final Map<String, String> LOCATION_ALIASES = Map.ofEntries(
            Map.entry("bangalore", "bengaluru"),
            Map.entry("mumbai", "bombay"),
            Map.entry("delhi", "new delhi"),
            Map.entry("hyderabad", "secunderabad"),
            Map.entry("chennai", "madras"),
            Map.entry("gurgaon", "gurugram"),
            Map.entry("mysore", "mysuru"),
            Map.entry("mangalore", "mangaluru"),
            Map.entry("trivandrum", "thiruvananthapuram"),
            Map.entry("pondicherry", "puducherry"),
            Map.entry("goa", "panaji")
    );

    public ApiResponse<Map<String, Object>> searchJobs(String query, String location, String experience,
                                                       String jobType, String salary, String company,
                                                       String sort, int page, int limit) {
        List<Job> allJobs = jobRepository.findAll();

        List<Job> filtered = allJobs.stream()
                .filter(job -> matchesQuery(job, query))
                .filter(job -> matchesLocation(job, location))
                .filter(job -> matchesExperience(job, experience))
                .filter(job -> matchesJobType(job, jobType))
                .filter(job -> matchesCompany(job, company))
                .filter(job -> matchesSalary(job, salary))
                .collect(Collectors.toList());

        List<Job> sorted = sortJobs(filtered, sort, query);

        int total = sorted.size();
        int start = (page - 1) * limit;
        List<Job> paginated = sorted.stream()
                .skip(start)
                .limit(limit)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("jobs", paginated);
        result.put("total", total);
        result.put("page", page);
        result.put("limit", limit);

        return ApiResponse.success(result);
    }

    private boolean matchesQuery(Job job, String query) {
        if (query == null || query.isBlank()) return true;
        String q = query.toLowerCase();
        return job.getTitle().toLowerCase().contains(q) ||
               job.getCompany().toLowerCase().contains(q) ||
               job.getDescription().toLowerCase().contains(q);
    }

    private boolean matchesLocation(Job job, String location) {
        if (location == null || location.isBlank()) return true;
        String loc = location.toLowerCase();
        String jobLoc = job.getLocation().toLowerCase();
        if (jobLoc.contains(loc) || loc.contains(jobLoc)) return true;
        String alias = LOCATION_ALIASES.get(loc);
        if (alias != null && jobLoc.contains(alias)) return true;
        return false;
    }

    private boolean matchesExperience(Job job, String experience) {
        if (experience == null || experience.isBlank()) return true;
        String exp = experience.toLowerCase();
        return job.getExperience().toLowerCase().contains(exp);
    }

    private boolean matchesJobType(Job job, String jobType) {
        if (jobType == null || jobType.isBlank()) return true;
        return job.getJobType().toLowerCase().contains(jobType.toLowerCase());
    }

    private boolean matchesCompany(Job job, String company) {
        if (company == null || company.isBlank()) return true;
        return job.getCompany().toLowerCase().contains(company.toLowerCase());
    }

    private boolean matchesSalary(Job job, String salary) {
        if (salary == null || salary.isBlank()) return true;
        return job.getSalary().toLowerCase().contains(salary.toLowerCase());
    }

    private List<Job> sortJobs(List<Job> jobs, String sort, String query) {
        if (query == null || query.isBlank()) {
            if ("salary".equalsIgnoreCase(sort)) {
                return jobs.stream()
                        .sorted(Comparator.comparingInt(j -> extractFirstNumber(j.getSalary())).reversed())
                        .collect(Collectors.toList());
            }
            if ("experience".equalsIgnoreCase(sort)) {
                return jobs.stream()
                        .sorted(Comparator.comparingInt(j -> extractFirstNumber(j.getExperience())).reversed())
                        .collect(Collectors.toList());
            }
        }
        return jobs;
    }

    private int extractFirstNumber(String text) {
        if (text == null) return 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(text);
        return m.find() ? Integer.parseInt(m.group()) : 0;
    }
}
