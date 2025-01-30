package com.mindfire.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mindfire.entity.Employee;

/**
 * Repository interface for accessing and managing employee data.
 * Extends JpaRepository to provide basic CRUD operations on the Employee entity.
 * Additionally, custom methods for specific employee queries are included here.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, CustomEmployeeRepository {
	
	List<Employee> findBySalaryGreaterThan(Long salary);

	List<Employee> findByDateOfJoiningAfter(LocalDate date);
	/**
     * Finds employees who have one of the top 3 distinct salaries in the company.
     * This query joins the employee table with a subquery that selects the top 3 distinct salaries.
     * 
     * @return a list of employees with the top 3 distinct salaries
     */
	@Query(value = "SELECT e.* FROM employee e "
			+ "JOIN (SELECT DISTINCT salary FROM employee ORDER BY salary DESC LIMIT 3) top_salaries "
			+ "ON e.salary = top_salaries.salary", nativeQuery = true)
	List<Employee> findEmployeesWithTop3DistinctSalaries();

}
