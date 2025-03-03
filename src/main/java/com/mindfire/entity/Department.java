package com.mindfire.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a department entity in the system.
 * Each department has a unique name, a location, and can have many employees assigned to it.
 * 
 * The Department entity is mapped to the database table "department", and has a many-to-many relationship
 * with the Employee entity via the "employee_department" join table.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
		uniqueConstraints = @UniqueConstraint(
				name = "name_unique",
				columnNames = "name"
				)
		)
public class Department {
	/**
     * The unique identifier for the department.
     * The ID is auto-generated.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	/**
     * The name of the department.
     * It must be unique across all departments.
     */
	private String name;
	/**
     * The location of the department.
     * This represents where the department is physically located.
     */
	private String location;
	/**
     * A list of employees associated with this department.
     * This represents a many-to-many relationship with the Employee entity.
     */
	@ManyToMany
	@JoinTable(
		      name = "employee_department", 
		      joinColumns = @JoinColumn(name = "department_id"), 
		      inverseJoinColumns = @JoinColumn(name = "employee_id")
		    )
	@JsonIgnore
	private List<Employee> employees=new ArrayList<>();
}
