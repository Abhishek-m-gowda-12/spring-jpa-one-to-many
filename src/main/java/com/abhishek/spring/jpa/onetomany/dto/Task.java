package com.abhishek.spring.jpa.onetomany.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private Timestamp createdDate;
    private Set<SubTask> subTasks;
}
