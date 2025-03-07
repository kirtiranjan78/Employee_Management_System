package com.mindfire.service.impl;


import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mindfire.client.PerformanceClient;
import com.mindfire.constant.MessageConstant;
import com.mindfire.entity.Department;
import com.mindfire.entity.Employee;
import com.mindfire.exception.CustomFeignException;
import com.mindfire.exception.EmployeeNotFoundException;
import com.mindfire.exception.PerformanceNotAddedException;
import com.mindfire.model.PerformanceResponse;
import com.mindfire.repo.CustomEmployeeRepository;
import com.mindfire.repo.DepartmentRepository;
import com.mindfire.repo.EmployeeRepository;
import com.mindfire.service.EmployeeService;

import feign.FeignException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
/***
 * EmployeeServiceImpl class implements EmployeeService interface  
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private PerformanceClient performanceClient;
	
	
	/***
	 * Used to add employee to Employee Entity.
	 * @param employee
	 * @return {@link Employee}
	 */
	@Override
	@Transactional
	public Employee addEmployee(Employee employee) {
		for (Department department : employee.getDepartments()) {
            if (department.getId() == 0) {
                // New department, so we save it
                departmentRepository.save(department);
            } else {
                // Existing department, fetch it from the database to ensure it's managed
                Department managedDepartment = departmentRepository.findById(department.getId()).orElse(null);
                if (managedDepartment != null) {
                    department = managedDepartment; // Re-attach the department
                } else {
                    throw new IllegalArgumentException("Department with ID " + department.getId() + " not found.");
                }
            }

            // Add the employee to the department (bi-directional relationship)
            if (!department.getEmployees().contains(employee)) {
                department.getEmployees().add(employee);
            }
        }

        // Save or update the employee, now that all departments are properly attached
        return employeeRepository.save(employee);
	}
	/***
	 * Used to update employee salary.
	 * @param id
	 * @param salary
	 * @return {@link Employee}
	 */
	@Override
	public Employee updateEmployeeSalary(int id, double salary) {
		Optional<Employee> employee=employeeRepository.findById(id);
		if(employee.isPresent()) {
			Employee emp = employee.get();
			emp.setSalary(salary);
			employeeRepository.save(emp);
			return emp;
		}
		
		return null;
	}
	/***
	 * Used to delete an employee from Employee entity.
	 * @param id
	 */
	@Override
	public void deleteEmployee(int id) {
		if(removeEmployeeFromDepartment(id))
		employeeRepository.deleteById(id);
		
	}
	/***
	 * Used to fetch all employee list.
	 * @return List<{@link Employee}
	 */
	@Override
	@Transactional
	public List<Employee> getAllEmployees() {
		List<Employee> list = employeeRepository.findAll();
		return list;
	}
	/***
	 * Used to assign a department to an employee.
	 * @param emp_id
	 * @param dep_id
	 * @return {@link Employee}
	 */
	@Override
	public Employee assignDepartment(int emp_id, int dep_id) {
		Optional<Department> dep = departmentRepository.findById(dep_id);
		Optional<Employee> emp = employeeRepository.findById(emp_id);
		if(dep.isPresent() && emp.isPresent()) {
			Department department = dep.get();
			Employee employee = emp.get();
			employee.getDepartments().add(department);
			department.getEmployees().add(employee);
			departmentRepository.save(department);
			employeeRepository.save(employee);
			return employee;
		}
		return null;
	}
	/***
	 * Used to remove an employee from a department.
	 * @param emp_id
	 * @return {@link Boolean}
	 */
	public boolean removeEmployeeFromDepartment(int emp_id) {
		Employee employee = employeeRepository.findById(emp_id).get();
		if(employee!=null) {
			List<Department> dep=employee.getDepartments();
			for (Department department : dep) {
				department.getEmployees().remove(employee);
			}
		employeeRepository.save(employee);
		return true;
		}
		return false;
	}

	@Override
	/***
	 * Used to fetch employee by salary greater than a given amount.
	 * @param salary
	 * @return List<{@link Employee}
	 */
	public List<Employee> getAllEmployeesBySalary(long salary) {
		List<Employee> list = employeeRepository.findBySalaryGreaterThan(salary);
		return list;
	}

	@Override
	/***
	 * Used to Fetch employees who joined in the last 6 months. 
	 * @param date
	 * @return {@link List<Employee>}
	 */
	public List<Employee> getAllEmployeeByJoiningDate(LocalDate date) {
		List<Employee> list = employeeRepository.findByDateOfJoiningAfter(date.minusMonths(6));
		return list;
	}

	@Override
	/***
	 * Used to fetch the top 3 highest-paid employees.
	 * @return {@link List<Employee>}
	 */
	public List<Employee> getTop3HighestPaidEmployees() {
		List<Employee> list = employeeRepository.findEmployeesWithTop3DistinctSalaries();
		return list;
	}

	@Override
	/***
	 * Used to retrieve employees ordered by salary in descending order in pages.
	 * @param pageNo
	 * @param pageSize
	 * @return {@link List<Employee>}
	 */
	public Page<Employee> getEmployeesInBatches(int pageNo, int pageSize) {
		Pageable pageable=PageRequest.of(pageNo, pageSize, Sort.by("salary").descending());
		Page<Employee> page = employeeRepository.findAll(pageable);
		return page;
	}

	@Override
	/***
	 * Used to transfer employees from one department to another.
	 * @param emp_id
	 * @param old_dep_id
	 * @param new_dep_id
	 */
	@Transactional
	public void employeeTransfer(int emp_id, int old_dep_id, int new_dep_id) {
		Department oldDepartment=departmentRepository.findById(old_dep_id).get();
		Department newDepartment=departmentRepository.findById(new_dep_id).get();
		Employee employee=employeeRepository.findById(emp_id).get();
		if(oldDepartment!=null && newDepartment!=null && employee!=null) {
		employee.getDepartments().add(newDepartment);
		newDepartment.getEmployees().add(employee);
		employee.getDepartments().remove(oldDepartment);
		oldDepartment.getEmployees().remove(employee);
		departmentRepository.save(newDepartment);
		departmentRepository.save(oldDepartment);
		employeeRepository.save(employee);
		}
	}	

	@Override
	/***
	 * Used for bulk salary updates of employees.
	 * @param percentage
	 */
	public void updateBulkSalary(double percentage) {
		employeeRepository.bulkSalaryGenerate(percentage);
	}
	@Override
	public Employee getEmployeeById(int id) {
		Employee employee=employeeRepository.findById(id).get();
		return employee;
	}
	@Override
	@Transactional
	public Employee updatEmployee(int id, Employee employee) {
		Employee emp=employeeRepository.findById(id).get();
		if(emp!=null) {
			emp.setName(employee.getName());
			emp.setEmail(employee.getEmail());
			emp.setSalary(employee.getSalary());
			emp.setDateOfJoining(employee.getDateOfJoining());
			emp.setDepartments(employee.getDepartments());
		}
		
		return addEmployee(emp);
	}
	/**
     * Retrieves the performance records for a given employee by their ID.
     * 
     * @param id The ID of the employee for whom performance records are to be fetched.
     * @return A list of {@link PerformanceResponse} representing the performance records of the employee.
     * @throws EmployeeNotFoundException if the employee with the given ID is not found.
     */
    @Override
    public List<PerformanceResponse> getPerformanceByEmployeeId(int id) {
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(MessageConstant.EMPLOYEE_NOT_FOUND+id));
        List<PerformanceResponse> list = performanceClient.getPerformanceById(id);
        if(list.size()==0) {
        	throw new PerformanceNotAddedException(MessageConstant.FEEDBACK_NOT_ADDED+id);
        }
        else {
        return list;
        }
    }

    /**
     * Adds a new performance record to an employee's profile.
     * 
     * @param performanceResponse The {@link PerformanceResponse} object that holds the performance data to be added.
     * @param id The ID of the employee to whom the performance record will be added.
     * @return A {@link PerformanceResponse} object representing the added performance record.
     * @throws EmployeeNotFoundException if the employee with the given ID is not found.
     * @throws CustomFeignException if an error occurs while interacting with the performance service.
     */
    @Override
    public PerformanceResponse addPerformanceToEmployee(PerformanceResponse performanceResponse, int id) {
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(MessageConstant.EMPLOYEE_NOT_FOUND+id));
        
        try {
            PerformanceResponse response = performanceClient.addPerformanceResponse(performanceResponse, id);
            return response;
        } catch (FeignException e) {
            String errorMessage = e.contentUTF8();
            
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = MessageConstant.UNEXPECTED_ERROR;
            }
            throw new CustomFeignException(MessageConstant.FEEDBACK_ADD_ERROR + errorMessage);
        }
    }
	
	
}
