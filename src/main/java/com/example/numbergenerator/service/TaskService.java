package com.example.numbergenerator.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.example.numbergenerator.dtos.TaskDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface TaskService {

	String createTask(List<TaskDTO> taskDTOs) throws JsonMappingException, JsonProcessingException,
			InterruptedException, ExecutionException, TimeoutException;

	List<String> getTaskStatusForUUID(String uuid);

	List<String> getNumListForUUID(String uuid);

	

}
