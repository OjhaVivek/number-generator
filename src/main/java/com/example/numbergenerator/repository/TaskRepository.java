package com.example.numbergenerator.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.numbergenerator.entity.Task;
import com.example.numbergenerator.entity.TaskId;

@Repository
public interface TaskRepository extends CrudRepository<Task, TaskId> {

	List<Task> findAllByTaskIdId(final String id);

}
