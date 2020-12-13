package com.example.numbergenerator.dtos;

import java.io.Serializable;

public class TaskDTO implements Serializable {

	private static final long serialVersionUID = 3147707768615054765L;

	private Integer goal;
	
	private Integer step;

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
	
	
}
