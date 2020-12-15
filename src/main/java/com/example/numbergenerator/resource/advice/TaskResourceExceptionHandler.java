package com.example.numbergenerator.resource.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.numbergenerator.exception.InternalServerException;
import com.example.numbergenerator.exception.InvalidRequestException;
import com.example.numbergenerator.exception.ResourceNotFoundException;

@ControllerAdvice
public class TaskResourceExceptionHandler {

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<String> handle(InvalidRequestException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handle(ResourceNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<String> handle(InternalServerException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
