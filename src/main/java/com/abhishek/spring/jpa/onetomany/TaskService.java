package com.abhishek.spring.jpa.onetomany;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    void addTasks(List<Task> tasks);

    Page<Task> getWithPaginationAndSorting(Pageable pageable);
}
