package com.example.numbergenerator.entity;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TaskId implements Serializable {
	
	private static final long serialVersionUID = -8511461744381191854L;
	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1, value = "id")
	public String id;
	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2, value = "sequence_number")
	public Integer sequenceNumber;

	public TaskId() {
		super();
	}

	public TaskId(String id, Integer sequenceNumber) {
		super();
		this.id = id;
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return "TaskId [id=" + id + ", sequenceNumber=" + sequenceNumber + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}