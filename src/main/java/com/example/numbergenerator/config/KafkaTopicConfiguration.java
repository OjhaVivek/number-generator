package com.example.numbergenerator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfiguration {

	@Bean
	public NewTopic task() {
		return new NewTopic("task", 3, (short) 2);
	}

}

