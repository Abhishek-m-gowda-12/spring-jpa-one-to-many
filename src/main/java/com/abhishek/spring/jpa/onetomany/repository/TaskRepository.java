package com.abhishek.spring.jpa.onetomany.repository;

import com.abhishek.spring.jpa.onetomany.entity.TaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long>, PagingAndSortingRepository<TaskEntity, Long> {

}
