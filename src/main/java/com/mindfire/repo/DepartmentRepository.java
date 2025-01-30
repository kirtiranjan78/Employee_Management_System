package com.mindfire.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.entity.Department;
/**
 * Repository interface for accessing and managing department data.
 * Extends JpaRepository to provide basic CRUD operations on the Department entity.
 * This repository is used to interact with the "department" table in the database.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	
}
