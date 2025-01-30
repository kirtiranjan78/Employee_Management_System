package com.mindfire.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.repo.CustomEmployeeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
/**
 * Implementation of the CustomEmployeeRepository interface.
 * This class contains custom logic for handling employee-related operations
 * that extend the basic functionality  such as bulk updating employee salaries.
 */
@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {
	
	@Autowired
	private EntityManager entityManager;
	/**
     * Bulk updates the salary of all employees by the given percentage.
     * The salaries are updated by multiplying each employee's salary with a multiplier,
     * which is calculated using the provided percentage.
     * 
     * @param percentage the percentage by which the employee salaries will be increased
     *                   (e.g., 10 for a 10% increase).
     *                   The salary will be multiplied by (1 + percentage / 100).
     */
	@Transactional
	@Override
	public void bulkSalaryGenerate(double percentage) {
		double multiplier = 1 + (percentage / 100);

		String jpql = "UPDATE Employee e SET e.salary = ROUND(e.salary * :multiplier, 2)";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("multiplier", multiplier);

		query.executeUpdate();
		
	}
		
}
