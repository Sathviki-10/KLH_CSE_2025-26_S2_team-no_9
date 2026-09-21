package com.smartjobportal.service;

import com.smartjobportal.model.Job;
import com.smartjobportal.model.SavedJob;
import com.smartjobportal.model.Skill;
import com.smartjobportal.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final List<Skill> skills = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Job> jobs = new ArrayList<>();
    private final List<SavedJob> savedJobs = new ArrayList<>();
    private final Map<Long, List<Skill>> jobSkills = new ConcurrentHashMap<>();
    private final AtomicLong userIdSeq = new AtomicLong(0);
    private final AtomicLong savedJobIdSeq = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        loadSkills();
        loadUsers();
        loadJobs();
        loadSavedJobs();
        if (jobs.isEmpty()) {
            throw new IllegalStateException("No jobs loaded from data/jobs.txt");
        }
    }

    private void loadSkills() {
        try {
            ClassPathResource resource = new ClassPathResource("data/skills.txt");
            InputStream is = resource.getInputStream();
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isBlank()) {
                        lines.add(line);
                    }
                }
            }
            long id = 1;
            for (String skillName : lines) {
                skills.add(new Skill(id++, skillName));
            }
        } catch (Exception e) {
            System.err.println("Error loading skills: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try {
            ClassPathResource resource = new ClassPathResource("data/users.txt");
            InputStream is = resource.getInputStream();
            List<Map<String, Object>> rawUsers = objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> raw : rawUsers) {
                User user = new User();
                user.setId(((Number) raw.get("id")).longValue());
                user.setName((String) raw.get("name"));
                user.setEmail((String) raw.get("email"));
                user.setPassword((String) raw.get("password"));
                user.setLocation((String) raw.getOrDefault("location", ""));
                user.setExperience((String) raw.getOrDefault("experience", ""));
                user.setCreatedAt(LocalDateTime.now());
                users.add(user);
                long maxId = user.getId();
                if (maxId > userIdSeq.get()) {
                    userIdSeq.set(maxId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private void loadJobs() {
        try {
            ClassPathResource resource = new ClassPathResource("data/jobs.txt");
            InputStream is = resource.getInputStream();
            List<Map<String, Object>> rawJobs = objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> raw : rawJobs) {
                Job job = new Job();
                job.setId(((Number) raw.get("id")).longValue());
                job.setTitle((String) raw.get("title"));
                job.setCompany((String) raw.get("company"));
                job.setLocation((String) raw.get("location"));
                job.setDescription((String) raw.get("description"));
                job.setExperience((String) raw.get("experience"));
                job.setSalary((String) raw.get("salary"));
                job.setJobType((String) raw.get("job_type"));
                job.setCreatedAt(LocalDateTime.now());
                jobs.add(job);

                List<String> skillNames = (List<String>) raw.get("skills");
                if (skillNames != null) {
                    List<Skill> matched = skillNames.stream()
                            .map(String::trim)
                            .filter(name -> !name.isBlank())
                            .map(name -> skills.stream()
                                    .filter(s -> s.getSkillName().equalsIgnoreCase(name))
                                    .findFirst()
                                    .orElse(null))
                            .filter(Objects::nonNull)
                            .toList();
                    jobSkills.put(job.getId(), matched);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading jobs: " + e.getMessage());
        }
    }

    private void loadSavedJobs() {
        try {
            ClassPathResource resource = new ClassPathResource("data/saved_jobs.txt");
            InputStream is = resource.getInputStream();
            List<Map<String, Object>> rawSaved = objectMapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> raw : rawSaved) {
                SavedJob sj = new SavedJob();
                sj.setId(savedJobIdSeq.incrementAndGet());
                sj.setUserId(((Number) raw.get("user_id")).longValue());
                sj.setJobId(((Number) raw.get("job_id")).longValue());
                sj.setCreatedAt(LocalDateTime.now());
                savedJobs.add(sj);
            }
        } catch (Exception e) {
            System.err.println("Error loading saved jobs: " + e.getMessage());
        }
    }

    public List<Job> getAllJobs() {
        return new ArrayList<>(jobs);
    }

    public Optional<Job> getJobById(Long id) {
        return jobs.stream().filter(j -> j.getId().equals(id)).findFirst();
    }

    public List<Skill> getSkillsForJob(Long jobId) {
        return jobSkills.getOrDefault(jobId, Collections.emptyList());
    }

    public List<Job> searchJobs(String query, String skills, String location, String experience, String jobType, String salary, String company) {
        return jobs.stream()
                .filter(job -> matchesQuery(job, query))
                .filter(job -> matchesSkills(job, skills))
                .filter(job -> matchesLocation(job, location))
                .filter(job -> matchesExperience(job, experience))
                .filter(job -> matchesJobType(job, jobType))
                .filter(job -> matchesCompany(job, company))
                .filter(job -> matchesSalary(job, salary))
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(Job job, String query) {
        if (query == null || query.isBlank()) return true;
        String q = query.toLowerCase();
        return containsIgnoreCase(job.getTitle(), q) ||
               containsIgnoreCase(job.getCompany(), q) ||
               containsIgnoreCase(job.getDescription(), q);
    }

    private boolean matchesSkills(Job job, String skillsFilter) {
        if (skillsFilter == null || skillsFilter.isBlank()) return true;
        List<String> jobSkillNames = getSkillsForJob(job.getId()).stream()
                .map(Skill::getSkillName)
                .map(String::toLowerCase)
                .toList();
        return Arrays.stream(skillsFilter.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isBlank())
                .allMatch(requested -> jobSkillNames.stream()
                        .anyMatch(available -> available.contains(requested.toLowerCase()) || requested.toLowerCase().contains(available)));
    }

    private boolean matchesLocation(Job job, String location) {
        if (location == null || location.isBlank()) return true;
        String loc = location.toLowerCase();
        String jobLoc = safeLower(job.getLocation());
        if (jobLoc.contains(loc) || loc.contains(jobLoc)) return true;
        Map<String, String> aliases = Map.ofEntries(
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
        String alias = aliases.get(loc);
        if (alias != null && jobLoc.contains(alias)) return true;
        return false;
    }

    private boolean matchesExperience(Job job, String experience) {
        if (experience == null || experience.isBlank()) return true;
        return containsIgnoreCase(job.getExperience(), experience);
    }

    private boolean matchesJobType(Job job, String jobType) {
        if (jobType == null || jobType.isBlank()) return true;
        return containsIgnoreCase(job.getJobType(), jobType);
    }

    private boolean matchesCompany(Job job, String company) {
        if (company == null || company.isBlank()) return true;
        return containsIgnoreCase(job.getCompany(), company);
    }

    private boolean matchesSalary(Job job, String salary) {
        if (salary == null || salary.isBlank()) return true;
        return containsIgnoreCase(job.getSalary(), salary);
    }

    private boolean containsIgnoreCase(String value, String query) {
        return safeLower(value).contains(safeLower(query));
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    public User authenticate(String email, String password) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUserById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User addUser(User user) {
        long newId = userIdSeq.incrementAndGet();
        if (newId <= 0) {
            newId = users.stream().mapToLong(User::getId).max().orElse(0L) + 1;
        }
        user.setId(newId);
        user.setCreatedAt(LocalDateTime.now());
        users.add(user);
        return user;
    }

    public List<SavedJob> getSavedJobsByUser(Long userId) {
        return savedJobs.stream()
                .filter(sj -> sj.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public SavedJob saveJob(Long userId, Long jobId) {
        Optional<SavedJob> existing = savedJobs.stream()
                .filter(sj -> sj.getUserId().equals(userId) && sj.getJobId().equals(jobId))
                .findFirst();
        if (existing.isPresent()) {
            return existing.get();
        }
        SavedJob sj = new SavedJob(savedJobIdSeq.incrementAndGet(), userId, jobId, LocalDateTime.now());
        savedJobs.add(sj);
        return sj;
    }

    public void unsaveJob(Long userId, Long jobId) {
        savedJobs.removeIf(sj -> sj.getUserId().equals(userId) && sj.getJobId().equals(jobId));
    }

    public List<Job> getSimilarJobs(Long excludeJobId) {
        return jobs.stream()
                .filter(j -> !j.getId().equals(excludeJobId))
                .limit(4)
                .collect(Collectors.toList());
    }

    public List<Skill> getAllSkills() {
        return new ArrayList<>(skills);
    }
}
