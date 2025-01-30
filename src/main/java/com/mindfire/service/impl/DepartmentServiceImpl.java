package com.mindfire.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.entity.Department;
import com.mindfire.entity.Employee;
import com.mindfire.repo.DepartmentRepository;
import com.mindfire.service.DepartmentService;
/***
 * DepartmentServiceImpl class implements DepartmentService interface  
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	/***
	 * Used to add department to Department entities.
	 * @param department
	 * @return {@link Department}
	 */
	public Department addDepartment(Department department) {
		Department dep = departmentRepository.save(department);
		return dep;
	}

	@Override
	/***
	 * Used to delete an department from Department entity.
	 * @param id
	 */
	public void deleteDepartment(int id) {
		if(removeDepartmentFromEmployee(id)) {
		departmentRepository.deleteById(id);
		}
	}
	
	/***
	 * Used to remove Department from Employee entity.
	 * @param dep_id
	 * @return Boolean
	 */
	public boolean removeDepartmentFromEmployee(int dep_id) {
		Department department = departmentRepository.findById(dep_id).get();
		if(department!=null) {
			List<Employee> emp=department.getEmployees();
			for (Employee employee : emp) {
				employee.getDepartments().remove(department);
			}
		departmentRepository.save(department);
		return true;
		}
		return false;
	}

	@Override
	/***
	 * Used to Fetch the total number of employees in each department.
	 * @return HashMap<String,Integer>
	 */
	public HashMap<String, Integer> countEmployeeOfEachDepartment() {
		HashMap<String, Integer> map=new HashMap<>();
		List<Department> list=departmentRepository.findAll();
		for (Department department : list) {
			map.put(department.getName(), department.getEmployees().size());
		}
		return map;
	}
	
	
}
