package com.smartjobportal.repository;

import com.smartjobportal.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findBySkillNameIgnoreCase(String skillName);
}
