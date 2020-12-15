package com.example.numbergenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.example.numbergenerator.dtos.TaskDTO;
import com.example.numbergenerator.entity.Task;
import com.example.numbergenerator.entity.TaskStatus;
import com.example.numbergenerator.repository.TaskRepository;
import com.example.numbergenerator.service.impl.TaskServiceImpl;

@SpringBootTest(classes = TaskServiceImpl.class)
class TaskServiceTest {

	@MockBean
	private TaskRepository taskRepository;

	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private TaskService taskService;

	@Test
	void testCreateTaskValid() throws Exception {
		ListenableFuture<SendResult<String, String>> responseFuture = mock(ListenableFuture.class);
		when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString())).thenReturn(responseFuture);

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(2);

		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setGoal(100);
		taskDTO1.setStep(3);

		taskService.createTask(Arrays.asList(taskDTO, taskDTO1));
		verify(kafkaTemplate, times(1)).send(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void testCreateTaskInvalidRuntimeException() throws Exception {
		when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException());

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setGoal(10);
		taskDTO.setStep(2);

		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setGoal(100);
		taskDTO1.setStep(3);

		assertThrows(RuntimeException.class, () -> taskService.createTask(Arrays.asList(taskDTO, taskDTO1)));
	}

	@Test
	void testGetTaskStatusForUUIDValid() throws Exception {
		String uuid = UUID.randomUUID().toString();
		Task task1 = new Task();
		task1.setStatus(TaskStatus.IN_PROGRESS);
		Task task2 = new Task();
		task2.setStatus(TaskStatus.ERROR);
		Task task3 = new Task();
		task3.setStatus(TaskStatus.SUCCESS);
		
		List<String> expected = Arrays.asList(TaskStatus.IN_PROGRESS.getValue(), TaskStatus.ERROR.getValue(),
				TaskStatus.SUCCESS.getValue());
		when(taskRepository.findAllByTaskIdId(uuid)).thenReturn(Arrays.asList(task1, task2, task3));
		
		List<String> actual = taskService.getTaskStatusForUUID(uuid);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetNumListForUUIDValid() throws Exception {
		String uuid = UUID.randomUUID().toString();
		Task task1 = new Task();
		task1.setNumberList("10,8,6,4,2,0");
		Task task2 = new Task();
		task2.setNumberList("3,2,1,0");

		
		List<String> expected = Arrays.asList("10,8,6,4,2,0", "3,2,1,0");
		when(taskRepository.findAllByTaskIdId(uuid)).thenReturn(Arrays.asList(task1, task2));
		
		List<String> actual = taskService.getNumListForUUID(uuid);
		
		assertEquals(expected, actual);
	}
}
