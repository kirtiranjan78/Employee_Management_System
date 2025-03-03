package com.mindfire.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mindfire.model.PerformanceResponse;


@FeignClient(name = "PERFORMANCE-SERVICE")
public interface PerformanceClient {
	@GetMapping("/performance/getByEmployeeId/{id}")
	List<PerformanceResponse> getPerformanceById(@PathVariable("id") int id);
	@PostMapping("/performance/addPerformance/{employeeId}")
	PerformanceResponse addPerformanceResponse(@RequestBody PerformanceResponse performanceResponse,@PathVariable int employeeId);
}
