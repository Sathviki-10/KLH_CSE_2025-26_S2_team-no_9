package com.smartjobportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    private Long id;
    private String title;
    private String company;
    private String location;
    private String description;
    private String experience;
    private String salary;
    private String jobType;
    private LocalDateTime createdAt;
}
