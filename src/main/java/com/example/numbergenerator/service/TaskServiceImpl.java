package com.example.numbergenerator.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.numbergenerator.common.Constants;
import com.example.numbergenerator.dtos.TaskDTO;
import com.example.numbergenerator.entity.Task;
import com.example.numbergenerator.entity.TaskId;
import com.example.numbergenerator.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskServiceImpl implements TaskService {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private TaskRepository taskRepository;

	@Override
	public String createTask(List<TaskDTO> taskDTOs) throws JsonMappingException, JsonProcessingException,
			InterruptedException, ExecutionException, TimeoutException {
		UUID uuid = UUID.randomUUID();
		List<Task> tasks = new ArrayList<>();
		Date curDate = new Date();
		for (int i = 0; i < taskDTOs.size(); i++) {
			Task task = new Task();
			task.setTaskId(new TaskId(uuid.toString(), i));
			task.setGoal(taskDTOs.get(i).getGoal());
			task.setStep(taskDTOs.get(i).getStep());
			task.setCreateTs(curDate);
			task.setLastChangeTs(curDate);
			tasks.add(task);
		}
		kafkaTemplate.send(Constants.TOPIC_NAME, objectMapper.writeValueAsString(tasks))
				.get(Constants.KAFKA_SEND_WAIT_TIME, TimeUnit.SECONDS);
		return uuid.toString();

	}

	@Override
	public List<String> getTaskStatusForUUID(String uuid) {
		List<Task> tasks = taskRepository.findAllByTaskIdId(uuid);
		return tasks.stream().map(task -> task.getStatus().getValue()).collect(Collectors.toList());
	}

	@Override
	public List<String> getNumListForUUID(String uuid) {
		List<Task> tasks = taskRepository.findAllByTaskIdId(uuid);
		return tasks.stream().map(Task::getNumberList).collect(Collectors.toList());
	}
}
