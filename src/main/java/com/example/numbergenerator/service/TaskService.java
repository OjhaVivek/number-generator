package com.example.numbergenerator.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.example.numbergenerator.dtos.TaskDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

/*
 *  
 * Service signature used by resource layer to interact with other service like kafka and cassandra.
 */
public interface TaskService {

	/**
	 * Handles the logic on creating the tasks.
	 * @param taskDTOs - Validated messages received By APIs
	 * @return id with which document is created
	 * @throws JsonProcessingException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	String createTask(List<TaskDTO> taskDTOs)
			throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Fetches status of all the tasks associated with the provided id.
	 * @param uuid - id of the tasks
	 * @return list of statuses.
	 */
	List<String> getTaskStatusForUUID(String uuid);

	/**
	 * Fetches number list generated for all the tasks associated with the provided id.
	 * @param uuid - id of the tasks
	 * @return list of number lists
	 */
	List<String> getNumListForUUID(String uuid);

}
