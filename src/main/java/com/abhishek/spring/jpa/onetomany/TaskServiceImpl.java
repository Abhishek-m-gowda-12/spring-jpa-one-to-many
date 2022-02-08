package com.abhishek.spring.jpa.onetomany;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        List<TaskEntity> taskEntities = (List<TaskEntity>) taskRepository.findAll();

        return Optional.ofNullable(taskEntities).orElse(Collections.emptyList())
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
    }

    @Override
    public void addTasks(List<Task> tasks) {

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
    }

    @Override
    public Page<Task> getWithPaginationAndSorting(Pageable pageable) {
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
        return new PageImpl(tasks, pageable, page.getTotalElements());
    }
}
