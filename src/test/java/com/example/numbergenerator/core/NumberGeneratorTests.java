package com.example.numbergenerator.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.Acknowledgment;

import com.example.numbergenerator.entity.Task;
import com.example.numbergenerator.repository.TaskRepository;

@SpringBootTest(classes = NumberGenerator.class)
class NumberGeneratorTests {

	private static final String SAMPLE_MESSAGE = "[{\"taskId\":{\"id\":\"2bf23da4-efda-4524-8bbe-a9720727cf25\",\"sequenceNumber\":0},\"goal\":10,\"step\":2,\"status\":\"IN_PROGRESS\",\"createTs\":1608036714242,\"lastChangeTs\":1608036714242,\"numberList\":null}]";

	@MockBean
	private TaskRepository taskRepository;

	@Autowired
	private NumberGenerator numberGenerator;

	@Test
	void testProcessTasks() throws Exception {
		Acknowledgment acknowledgment = mock(Acknowledgment.class);
		ConsumerRecord<String, String> consumerRecord = mock(ConsumerRecord.class);
		when(consumerRecord.value()).thenReturn(
				SAMPLE_MESSAGE);
		when(taskRepository.save(Mockito.any())).thenReturn(new Task());
		
		numberGenerator.processTasks(consumerRecord, acknowledgment, 0);
		verify(acknowledgment, times(1)).acknowledge();
	}
	
	@Test
	void testProcessTasksInvalidThrowsException() throws Exception {
		Acknowledgment acknowledgment = mock(Acknowledgment.class);
		ConsumerRecord<String, String> consumerRecord = mock(ConsumerRecord.class);
		when(consumerRecord.value()).thenReturn(
				SAMPLE_MESSAGE);
		when(taskRepository.save(Mockito.any())).thenThrow(new RuntimeException());
		
		assertThrows(RuntimeException.class, () -> numberGenerator.processTasks(consumerRecord, acknowledgment, 0));
		
	}
}