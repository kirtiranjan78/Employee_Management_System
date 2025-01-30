package com.mindfire.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindfire.entity.Employee;
import com.mindfire.service.EmployeeService;
import jakarta.validation.Valid;

/**
 * REST controller for managing employee-related operations.
 * Provides endpoints to add, update, delete, retrieve employees,
 * as well as special operations such as transferring employees
 * between departments and updating salaries in bulk.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	/**
     * EndPoint to add a new employee.
     * 
     * @param employee the employee details to be added
     * @return a ResponseEntity containing the created employee and HTTP status CREATED
     */
	@PostMapping("/add")
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		Employee emp = employeeService.addEmployee(employee);
		return new ResponseEntity<Employee>(emp, HttpStatus.CREATED);
	}
	/**
     * EndPoint to update the salary of an existing employee.
     * 
     * @param id the ID of the employee whose salary will be updated
     * @param salary the new salary to be assigned to the employee
     * @return a ResponseEntity containing the updated employee and HTTP status OK
     */
	@PutMapping("/update/{id}")
	public ResponseEntity<Employee> updateEmployeeSalary(@PathVariable int id,@RequestParam double salary) {
		Employee emp = employeeService.updateEmployeeSalary(id, salary);
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to delete an employee by ID.
     * 
     * @param id the ID of the employee to be deleted
     */
	@DeleteMapping("/delete/{id}")
	public void deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
	}
	/**
     * EndPoint to retrieve all employees.
     * 
     * @return a ResponseEntity containing a list of all employees and HTTP status OK
     */
	@GetMapping("/get")
	public ResponseEntity<List<Employee>> getAllEmployee() {
		List<Employee> emp = employeeService.getAllEmployees();
		return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to assign an employee to a department.
     * 
     * @param emp_id the ID of the employee to be assigned
     * @param dep_id the ID of the department to which the employee will be assigned
     * @return a ResponseEntity containing the updated employee and HTTP status OK
     */
	@PutMapping("/assign/{emp_id}")
	public ResponseEntity<Employee> assignEmployeeToDepartment(@PathVariable int emp_id,@RequestParam int dep_id) {
		Employee emp = employeeService.assignDepartment(emp_id, dep_id);
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to retrieve employees with a salary greater than a given amount.
     * 
     * @param salary the salary threshold to filter employees
     * @return a ResponseEntity containing a list of employees with salaries greater than the specified value and HTTP status OK
     */
	@GetMapping("/getWithSalary/{salary}")
	public ResponseEntity<List<Employee>> getAllEmployeeWithSalaryGreaterThan(@PathVariable long salary) {
		List<Employee> emp = employeeService.getAllEmployeesBySalary(salary);
		return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to retrieve employees who joined in the last 6 months.
     * 
     * @return a ResponseEntity containing a list of employees who joined within the last 6 months and HTTP status OK
     */
	@GetMapping("/getWithDate")
	public ResponseEntity<List<Employee>> getAllEmployeeJoiningInLast6Months() {
		LocalDate date=LocalDate.now();
		List<Employee> emp = employeeService.getAllEmployeeByJoiningDate(date);
		return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to retrieve the top 3 highest-paid employees.
     * 
     * @return a ResponseEntity containing a list of the top 3 highest-paid employees and HTTP status OK
     */
	@GetMapping("/get-top3-highest-paid-employees")
	public ResponseEntity<List<Employee>> getTop3HighestPaidEmployees() {
		List<Employee> emp = employeeService.getTop3HighestPaidEmployees();
		return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
	}
	/**
     * EndPoint to retrieve employees in batches (pagination).
     * 
     * @param page the page number to fetch (default value is 0)
     * @return a ResponseEntity containing a list of employees for the requested page and HTTP status OK
     */
	@GetMapping("/get-in-batches")
	public ResponseEntity<List<Employee>> getEmployeeByPaging(@RequestParam(value = "page", defaultValue = "0") int page){
		return new ResponseEntity<List<Employee>>(employeeService.getEmployeesInBatches(page, 5),HttpStatus.OK);
	}
	/**
     * EndPoint to transfer an employee from one department to another.
     * 
     * @param emp_id the ID of the employee to be transferred
     * @param old_dep_id the ID of the current department
     * @param new_dep_id the ID of the new department
     * @return a ResponseEntity containing the updated list of employees and HTTP status OK
     */
	@GetMapping("/transfer")
	public ResponseEntity<List<Employee>> transferEmployee(@RequestParam(value = "emp_id") int emp_id,
														   @RequestParam(value = "old_dep_id") int old_dep_id,
														   @RequestParam(value = "new_dep_id") int new_dep_id){
		employeeService.employeeTransfer(emp_id, old_dep_id, new_dep_id);
		return new ResponseEntity<List<Employee>>(employeeService.getAllEmployees(),HttpStatus.OK);
	}
	/**
     * EndPoint to update the salaries of multiple employees in bulk.
     * 
     * @param percentage the percentage by which the employees' salaries will be increased
     * @return a ResponseEntity containing the updated list of employees and HTTP status OK
     */
	@PutMapping("/update/bulk")
	public ResponseEntity<List<Employee>> updateBulkSalaryOfEmployee(@RequestParam(value = "percentage") double percentage){
		employeeService.updateBulkSalary(percentage);
		return new ResponseEntity<List<Employee>>(employeeService.getAllEmployees(),HttpStatus.OK);
	}
	
}
