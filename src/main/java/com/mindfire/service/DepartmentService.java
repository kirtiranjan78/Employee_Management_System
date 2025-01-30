package com.mindfire.service;

import java.util.HashMap;

import com.mindfire.entity.Department;
/**
 * Service interface for managing department-related operations.
 * This interface provides methods to add, delete departments, and retrieve information 
 * about the number of employees in each department.
 */
public interface DepartmentService {
	
	Department addDepartment(Department department);
	
	void deleteDepartment(int id);
	
	HashMap<String, Integer> countEmployeeOfEachDepartment();
}
