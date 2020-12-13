package com.example.numbergenerator.resource.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.numbergenerator.exception.InternalServerException;
import com.example.numbergenerator.exception.InvalidRequestException;
import com.example.numbergenerator.exception.ResourceNotFoundException;

@ControllerAdvice
public class TaskResourceExceptionHandler {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRequestException.class)
	public void handle(InvalidRequestException e) {}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public void handle(ResourceNotFoundException e) {}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(InternalServerException.class)
	public void handle(InternalServerException e) {}
	
}
