package com.mindfire.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceResponse {
	private int id;
	private int rating;
	private String feedback;
	private int employeeId;
	
}
