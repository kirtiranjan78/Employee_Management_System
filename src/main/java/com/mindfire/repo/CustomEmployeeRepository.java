package com.mindfire.repo;

import org.springframework.stereotype.Repository;
/**
 * Custom repository interface for performing operations related to employee data.
 * This interface provides custom methods that extend standard CRUD functionality,
 * such as bulk salary updates.
 */
@Repository
public interface CustomEmployeeRepository {
	
	void bulkSalaryGenerate(double percentage);

}
