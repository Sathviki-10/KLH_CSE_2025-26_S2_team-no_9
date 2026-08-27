package com.smartjobportal.repository;

import com.smartjobportal.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUserId(Long userId);
    Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);
    void deleteByUserIdAndJobId(Long userId, Long jobId);
}
