package com.smartjobportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedJob {
    private Long id;
    private Long userId;
    private Long jobId;
    private LocalDateTime createdAt;
}
