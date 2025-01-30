package com.mindfire.controller;

import java.util.HashMap;
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

import com.mindfire.entity.Department;
import com.mindfire.entity.Employee;
import com.mindfire.service.impl.DepartmentServiceImpl;
import com.mindfire.service.impl.EmployeeServiceImpl;
/**
 * REST controller for managing department-related operations.
 * Provides endpoints to add, delete
 * as well as special operations such as counting employees
 * in each departments.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired
	private DepartmentServiceImpl departmentServiceImpl;
	
	/**
     * Add a new department.
     *
     * @param department
     * @return ResponseEntity containing the created Department and HTTP status code.
     */
	@PostMapping("/add")
	public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
		Department dep = departmentServiceImpl.addDepartment(department);
		return new ResponseEntity<Department>(dep, HttpStatus.CREATED);
	}
	/**
     * Delete a department by its ID.
     *
     * @param id 
     */
	@DeleteMapping("/delete/{id}")
	public void deleteDepartment(@PathVariable int id) {
		departmentServiceImpl.deleteDepartment(id);
	}
	/**
     * Get a count of employees for each department.
     *
     * @return ResponseEntity containing a map of department names and employee counts.
     */
	@GetMapping("/count")
	public ResponseEntity<HashMap<String, Integer>> getEmployeeOfEachDepartment(){
		HashMap<String,Integer> hashMap = departmentServiceImpl.countEmployeeOfEachDepartment();
		return new ResponseEntity<HashMap<String,Integer>>(hashMap,HttpStatus.OK);
	}
}
