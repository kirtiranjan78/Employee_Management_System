package com.mindfire.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mindfire.exception.CustomFeignException;
import com.mindfire.exception.EmployeeNotFoundException;
import com.mindfire.exception.PerformanceNotAddedException;
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
	/**
	 * Exception handler method for handling {@link EmployeeNotFoundException}.
	 * This method is invoked when an {@link EmployeeNotFoundException} is thrown during the execution
	 * of the application, typically when an employee with a specific ID is not found.
	 * 
	 * @param ex The {@link EmployeeNotFoundException} instance containing the exception details.
	 * @return A string message from the exception describing the error, to be returned to the client.
	 */
	@ExceptionHandler(EmployeeNotFoundException.class)
	public String handleInvalidEmployeeId(EmployeeNotFoundException ex){
		String string=ex.getMessage();
		return string;
	}
	/**
	 * Exception handler method for handling {@link CustomFeignException}.
	 * This method is invoked when a {@link CustomFeignException} is thrown, which may occur during 
	 * communication with external services, typically due to a failure in making a Feign client request.
	 * 
	 * @param ex The {@link CustomFeignException} instance containing the exception details.
	 * @return A string message from the exception describing the error, to be returned to the client.
	 */
	@ExceptionHandler(CustomFeignException.class)
	public String handleRtingValidation(CustomFeignException ex){
		String string=ex.getMessage();
		return string;
	}
	
	@ExceptionHandler(PerformanceNotAddedException.class)
	public String handleNullPerformance(PerformanceNotAddedException ex){
		String string=ex.getMessage();
		return string;
	}
	
}
