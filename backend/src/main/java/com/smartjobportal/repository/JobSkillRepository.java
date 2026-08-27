package com.smartjobportal.repository;

import com.smartjobportal.entity.JobSkill;
import com.smartjobportal.entity.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {
    List<JobSkill> findByJobId(Long jobId);
    List<JobSkill> findBySkillId(Long skillId);
}
