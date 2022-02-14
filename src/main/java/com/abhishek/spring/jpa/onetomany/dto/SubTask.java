package com.abhishek.spring.jpa.onetomany.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {
    private Long id;
    private String name;
    private String description;
    private Date createdDate;
    private Long taskId;
}
