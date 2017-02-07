package com.anand.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anand.demo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	Employee findByEmail(String email);
	
	List<Employee> findByLastName(String lastName);
	
	List<Employee> findByEmailOrFirstName(String email, String lastName);

}
