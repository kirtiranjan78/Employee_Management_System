package com.mindfire.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mindfire.entity.Department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

	private int id;

	private String name;

	private String email;

	private double salary;

	private LocalDate dateOfJoining;
	
	private List<Department> departments=new ArrayList<>();
	
	private List<PerformanceResponse> performanceResponses;
	
}
