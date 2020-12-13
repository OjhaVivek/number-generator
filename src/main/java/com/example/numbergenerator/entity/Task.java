package com.example.numbergenerator.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "task")
public class Task implements Serializable {

	private static final long serialVersionUID = 5714006666269057853L;

	@PrimaryKey
	private TaskId taskId;

	@Column(value = "goal")
	private Integer goal;
	
	@Column(value = "step")
	private Integer step;
	
	@Column(value = "status")
	private TaskStatus status = TaskStatus.IN_PROGRESS;
	
	@Column(value = "create_ts")
	private Date createTs;
	 
	@Column(value = "last_change_ts")
	private Date lastChangeTs;
	
	@Column(value = "number_list")
	private String numberList;

	public Task() {
		super();
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", goal=" + goal + ", step=" + step + ", status=" + status + ", createTs="
				+ createTs + ", lastChangeTs=" + lastChangeTs + ", numberList=" + numberList + "]";
	}

	public TaskId getTaskId() {
		return taskId;
	}

	public void setTaskId(TaskId taskId) {
		this.taskId = taskId;
	}


	public Date getLastChangeTs() {
		return lastChangeTs;
	}

	public void setLastChangeTs(Date lastChangeTs) {
		this.lastChangeTs = lastChangeTs;
	}

	public Integer getGoal() {
		return goal;
	}

	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public String getNumberList() {
		return numberList;
	}

	public void setNumberList(String numberList) {
		this.numberList = numberList;
	}
	
}
