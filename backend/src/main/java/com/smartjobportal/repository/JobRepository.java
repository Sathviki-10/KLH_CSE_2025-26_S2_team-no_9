package com.smartjobportal.repository;

import com.smartjobportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String title, String company, String location, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Job> search(@Param("query") String query);

    List<Job> findByLocationContainingIgnoreCase(String location);
}
