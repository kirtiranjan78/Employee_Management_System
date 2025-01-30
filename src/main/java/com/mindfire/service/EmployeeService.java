package com.mindfire.service;

import java.time.LocalDate;
import java.util.List;

import com.mindfire.entity.Department;
import com.mindfire.entity.Employee;
/**
 * Service interface for managing employee-related operations.
 * This interface includes methods for adding, updating, deleting employees, assigning departments,
 * and retrieving employee data based on various criteria (e.g., salary, joining date, etc.).
 */
public interface EmployeeService {
	
	Employee addEmployee(Employee employee);
	
	Employee updateEmployeeSalary(int id,double salary);
	
	void deleteEmployee(int id);
	
	List<Employee> getAllEmployees();
	
	Employee assignDepartment(int emp_id,int dep_id);
	
	List<Employee> getAllEmployeesBySalary(long salary);
	
	List<Employee> getAllEmployeeByJoiningDate(LocalDate date);
	
	List<Employee> getTop3HighestPaidEmployees();
	
	List<Employee> getEmployeesInBatches(int pageNo,int pageSize);
	
	void employeeTransfer(int emp_id,int old_dep_id,int new_dep_id);
	
	void updateBulkSalary(double percentage);
}
