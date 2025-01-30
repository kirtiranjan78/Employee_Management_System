package com.mindfire.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/***
 * EmployeeExceptionHandler class for handling exceptions in all the controller classes.
 */
@RestControllerAdvice
public class EmployeeExceptionHandler {
	
	/**
     * Handles validation exceptions when arguments are not valid (e.g., @Valid validation errors)
     */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArguements(MethodArgumentNotValidException ex){
		Map<String, String> map=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error->{
			map.put(error.getField(), error.getDefaultMessage());
		});
		return map;
	}
}
