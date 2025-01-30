package com.mindfire.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents an employee entity in the system.
 * Each employee has a unique identifier, a name, email, salary, and a joining date.
 * Employees are associated with one or more departments in a many-to-many relationship.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
		uniqueConstraints = @UniqueConstraint(
				name = "email_unique",
				columnNames = "email"
				)
		)
public class Employee {
	/**
     * The unique identifier for the employee.
     * The ID is auto-generated.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
     * The name of the employee.
     * This field cannot be null.
     */
	@NotNull(message = "Name can't be null")
	private String name;
	/**
     * The email address of the employee.
     * It must be a valid email format and unique across all employees.
     * This field cannot be null.
     */
	@Email(message = "Invalid email address")
	@NotNull(message = "email can't be null")
	private String email;
	/**
     * The salary of the employee.
     * The salary must be a positive value.
     */
	@Positive(message = "salary must be positive")
	private double salary;
	/**
     * The date when the employee joined the company.
     */
	private LocalDate dateOfJoining;
	
	@ManyToMany(mappedBy = "employees")
	private List<Department> departments;
}
