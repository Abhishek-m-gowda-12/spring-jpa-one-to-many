package com.abhishek.spring.jpa.onetomany;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("api = /task, method = GET, result = IN_PROGRESS");

        List<Task> tasks = taskService.getAllTasks();

        log.info("api = /task, method = GET, result = SUCCESS");
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody List<Task> tasks) {
        log.info("api = /task, method = POST, result = IN_PROGRESS");

        taskService.addTasks(tasks);

        log.info("api = /task, method = POST, result = SUCCESS");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/pagination")
    public ResponseEntity<Page<Task>> getWithPaginationAndSorting(@PageableDefault(size = 25, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("api = /task, method = POST, result = IN_PROGRESS");

        Page<Task> taskPage = taskService.getWithPaginationAndSorting(pageable);

        log.info("api = /task, method = POST, result = SUCCESS");
        return ResponseEntity.status(HttpStatus.OK).body(taskPage);
    }
}
