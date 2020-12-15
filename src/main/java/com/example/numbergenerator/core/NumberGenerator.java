package com.example.numbergenerator.core;

import java.util.Date;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * It contains the core logic of the application to generate descending numbers.
 * @author vivek
 *
 */
@Service
public class NumberGenerator {
	
	private Logger logger = LoggerFactory.getLogger(NumberGenerator.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private TaskRepository taskRepository;

	/**
	 * This method polls tasks from kafka. It then generates deceding numbers till 0 and stores it in cassandra.
	 * @param message - actual message in kafka
	 * @param acknowledgment - acknowledgement for manually acking the message on kafka
	 * @param partition - partition from which message was polled
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@KafkaListener(topics = Constants.TOPIC_NAME)
	public void processTasks(ConsumerRecord<String, String> message, Acknowledgment acknowledgment,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) throws JsonMappingException, JsonProcessingException {
		logger.info("Message polled: {}", message.value());
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
				task.setLastChangeTs(new Date());
			} catch (Exception e) {
				task.setStatus(TaskStatus.ERROR);
				logger.error("Message processing failure: {}", message.value());
			}
			taskRepository.save(task);
			
		});
		acknowledgment.acknowledge();
		logger.info("Message acknowledged: {}", message.value());
		
	}
}
