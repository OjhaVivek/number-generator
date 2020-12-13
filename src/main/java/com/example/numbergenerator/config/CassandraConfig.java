package com.example.numbergenerator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

//@Configuration
//@EnableCassandraRepositories(basePackages = "com.example.numbergenerator.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

	/*
	 * Provide a contact point to the configuration.
	 */
	@Override
	public String getContactPoints() {
		return "localhost";
	}

	/*
	 * Provide a keyspace name to the configuration.
	 */
	public String getKeyspaceName() {
		return "tasks";
	}
}
