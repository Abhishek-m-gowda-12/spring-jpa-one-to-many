package com.abhishek.spring.jpa.onetomany.service.impl;

import com.abhishek.spring.jpa.onetomany.dto.SubTask;
import com.abhishek.spring.jpa.onetomany.dto.Task;
import com.abhishek.spring.jpa.onetomany.entity.SubTaskEntity;
import com.abhishek.spring.jpa.onetomany.entity.TaskEntity;
import com.abhishek.spring.jpa.onetomany.repository.TaskRepository;
import com.abhishek.spring.jpa.onetomany.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        log.info("operation = getAllTasks, result = IN_PROGRESS");
        List<TaskEntity> taskEntities = (List<TaskEntity>) taskRepository.findAll();

        List<Task> tasks = Optional.ofNullable(taskEntities).orElse(Collections.emptyList())
                .stream()
                .map(taskEntity -> {
                    Task task = new Task();
                    BeanUtils.copyProperties(taskEntity, task);

                    Set<SubTask> subTasks = Optional.ofNullable(taskEntity.getSubTaskEntities()).orElse(Collections.emptySet())
                            .stream()
                            .map(subTaskEntity -> {
                                SubTask subTask = new SubTask();
                                BeanUtils.copyProperties(subTaskEntity, subTask);
                                return subTask;
                            }).collect(Collectors.toSet());

                    task.setSubTasks(subTasks);

                    return task;
                }).collect(Collectors.toList());

        log.info("operation = getAllTasks, result = SUCCESS");

        return tasks;
    }

    @Override
    public void addTasks(List<Task> tasks) {
        log.info("operation = addTasks, result = IN_PROGRESS");
        List<TaskEntity> taskEntities = tasks.stream().map(task -> {
            TaskEntity taskEntity = new TaskEntity();

            BeanUtils.copyProperties(task, taskEntity);

            Set<SubTaskEntity> subTaskEntities = Optional.ofNullable(task.getSubTasks()).orElse(Collections.emptySet())
                    .stream().map(subTask -> {
                        SubTaskEntity subTaskEntity = new SubTaskEntity();

                        BeanUtils.copyProperties(subTask, subTaskEntity);
                        subTaskEntity.setTaskEntity(taskEntity);

                        return subTaskEntity;
                    }).collect(Collectors.toSet());

            taskEntity.setSubTaskEntities(subTaskEntities);

            return taskEntity;
        }).collect(Collectors.toList());

        taskRepository.saveAll(taskEntities);
        log.info("operation = addTasks, result = SUCCESS");
    }

    @Override
    public Page<Task> getWithPaginationAndSorting(Pageable pageable) {
        log.info("operation = getWithPaginationAndSorting, result = IN_PROGRESS");
        Page<TaskEntity> page = taskRepository.findAll(pageable);

        List<Task> tasks = Optional.ofNullable(page.getContent()).orElse(Collections.emptyList())
                .stream()
                .map(taskEntity -> {
                    Task task = new Task();
                    BeanUtils.copyProperties(taskEntity, task);

                    Set<SubTask> subTasks = Optional.ofNullable(taskEntity.getSubTaskEntities()).orElse(Collections.emptySet())
                            .stream()
                            .map(subTaskEntity -> {
                                SubTask subTask = new SubTask();
                                BeanUtils.copyProperties(subTaskEntity, subTask);
                                return subTask;
                            }).collect(Collectors.toSet());

                    task.setSubTasks(subTasks);

                    return task;
                }).collect(Collectors.toList());
        log.info("operation = getWithPaginationAndSorting, result = SUCCESS");

        return new PageImpl<>(tasks, pageable, page.getTotalElements());
    }

    @Override
    public void deleteTasks(List<Long> taskIds) {
        log.info("operation = deleteTasks, result = IN_PROGRESS");
        taskRepository.deleteAllById(taskIds);
        log.info("operation = deleteTasks, result = SUCCESS");
    }

    @Override
    public void updateTasks(List<Task> tasks) {
        log.info("operation = updateTasks, result = IN_PROGRESS");

        List<Long> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());

        List<TaskEntity> taskEntities = (List<TaskEntity>) taskRepository.findAllById(taskIds);

        Map<Long, TaskEntity> taskEntityMap = Optional.ofNullable(taskEntities).orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(TaskEntity::getId, Function.identity()));

        List<TaskEntity> taskEntityToSave = new ArrayList<>();

        for (Task task : tasks) {
            TaskEntity taskEntity = taskEntityMap.getOrDefault(task.getId(), new TaskEntity());

            Optional.ofNullable(task.getId()).ifPresent(taskEntity::setId);
            Optional.ofNullable(task.getName()).ifPresent(taskEntity::setName);
            Optional.ofNullable(task.getDescription()).ifPresent(taskEntity::setDescription);

            Set<SubTaskEntity> subTaskEntities = Optional.ofNullable(task.getSubTasks()).orElse(Collections.emptySet())
                    .stream().map(subTask -> {
                        Map<Long, SubTaskEntity> subTaskEntityMap = Optional.ofNullable(taskEntity.getSubTaskEntities()).orElse(Collections.emptySet())
                                .stream().collect(Collectors.toMap(SubTaskEntity::getId, Function.identity()));

                        SubTaskEntity subTaskEntity = subTaskEntityMap.getOrDefault(subTask.getId(), new SubTaskEntity());

                        Optional.ofNullable(subTask.getId()).ifPresent(subTaskEntity::setId);
                        Optional.ofNullable(subTask.getName()).ifPresent(subTaskEntity::setName);
                        Optional.ofNullable(subTask.getDescription()).ifPresent(subTaskEntity::setDescription);

                        return subTaskEntity;
                    }).collect(Collectors.toSet());

            taskEntity.setSubTaskEntities(subTaskEntities);
            taskEntityToSave.add(taskEntity);
        }

        taskRepository.saveAll(taskEntityToSave);
        log.info("operation = updateTasks, result = SUCCESS");
    }
}
