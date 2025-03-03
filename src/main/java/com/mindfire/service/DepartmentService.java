package com.mindfire.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;

import com.mindfire.entity.Department;
import com.mindfire.entity.Employee;
/**
 * Service interface for managing department-related operations.
 * This interface provides methods to add, delete departments, and retrieve information 
 * about the number of employees in each department.
 */
public interface DepartmentService {
	
	Department addDepartment(Department department);
	
	List<Department> getAllDepartments();
	
	void deleteDepartment(int id);
	
	HashMap<String, Integer> countEmployeeOfEachDepartment();
	
	Page<Department> getDepartmentInBatches(int pageNo,int pageSize);
	
	Department getDepartmentById(int id);
	
	Department updatDepartment(int id,Department department);
}
