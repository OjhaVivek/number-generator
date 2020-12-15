package com.example.numbergenerator.core;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.example.numbergenerator.common.Constants;
import com.example.numbergenerator.entity.Task;
import com.example.numbergenerator.entity.TaskStatus;
import com.example.numbergenerator.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NumberGenerator {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TaskRepository taskRepository;

	@KafkaListener(topics = Constants.TOPIC_NAME)
	public void processTasks(ConsumerRecord<String, String> message, Acknowledgment acknowledgment,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) throws JsonMappingException, JsonProcessingException {
		List<Task> tasks = objectMapper.readValue(message.value(), new TypeReference<List<Task>>() {});

		tasks.stream().parallel().forEach(task -> {
			try {
				taskRepository.save(task);
				
				// Uncomment following code to simulate thread taking some time.
				// Thread.sleep(10000);
				
				StringBuilder numberListBuilder = new StringBuilder();
				int i=task.getGoal();
				while (i >= 0) {
					numberListBuilder.append(i + ",");
					i -= task.getStep();
				}
				numberListBuilder.setLength(numberListBuilder.length()-1);
				task.setNumberList(numberListBuilder.toString());
				task.setStatus(TaskStatus.SUCCESS);
			} catch (Exception e) {
				task.setStatus(TaskStatus.ERROR);
			}
			taskRepository.save(task);
			
		});
		acknowledgment.acknowledge();
		
		
	}
}
