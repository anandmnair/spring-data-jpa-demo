package com.anand.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.anand.demo.entity.Employee;
import com.anand.demo.repository.EmployeeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataJpaDemoApplication.class)
public class SpringDataJpaDemoApplicationTests {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Before
	public void resetRepository() {
		employeeRepository.deleteAll();
	}

	@Test
	public void addEmployee() {
		Employee employee = new Employee("fname", "lname", "fname.lname@email.com");
		employee = employeeRepository.save(employee);
		assertThat(employee.getEmpId(), is(notNullValue()));
	}

	@Test
	public void addFindById() {
		Employee employee = new Employee("fname", "lname", "fname.lname@email.com");
		employee = employeeRepository.save(employee);
		assertThat(employee.getEmpId(), is(notNullValue()));

		Employee employeeFromDB = employeeRepository.findOne(employee.getEmpId());
		assertThat(employeeFromDB.getEmpId(), is(employee.getEmpId()));
	}

	@Test
	public void findByEmail() {
		Employee employee = new Employee("fname", "lname", "fname.lname@email.com");
		employee = employeeRepository.save(employee);
		assertThat(employee.getEmpId(), is(notNullValue()));

		Employee employeeFromDB = employeeRepository.findByEmail("fname.lname@email.com");
		assertThat(employeeFromDB.getEmpId(), is(employee.getEmpId()));
		assertThat(employeeFromDB.getFirstName(), is("fname"));
		assertThat(employeeFromDB.getLastName(), is("lname"));
		assertThat(employeeFromDB.getEmail(), is("fname.lname@email.com"));
	}

	@Test
	public void findByLastName() {
		Employee employee = new Employee("fname1", "lname", "fname1.lname@email.com");
		employee = employeeRepository.save(employee);
		assertThat(employee.getEmpId(), is(notNullValue()));

		employee = new Employee("fname2", "lname", "fname2.lname@email.com");
		employee = employeeRepository.save(employee);
		assertThat(employee.getEmpId(), is(notNullValue()));

		List<Employee> employees = employeeRepository.findByLastName("lname");
		assertThat(employees, hasSize(2));
	}
	
	@Test
	public void paginationTest() {
		Employee employee = null;
		for(int i=1 ; i<=10; i++) {
			employee = new Employee("fname"+i, "lname"+i, i+"fname.lname@email.com");
			employee = employeeRepository.save(employee);
			assertThat(employee.getEmpId(), is(notNullValue()));
		}
		List<Employee> employees = employeeRepository.findAll();
		assertThat(employees, hasSize(10));
		
		Page<Employee> employeesPage = employeeRepository.findAll(new PageRequest(0, 3, new Sort("empId")));
		assertThat(employeesPage.isFirst(), is(true));
		assertThat(employeesPage.hasNext(), is(true));
		assertThat(employeesPage.getNumberOfElements(), is(3));
		assertThat(employeesPage.getTotalPages(), is(4));
		assertThat(employeesPage.getTotalElements(), is(10L));

		employeesPage = employeeRepository.findAll(employeesPage.nextPageable());
		assertThat(employeesPage.isFirst(), is(false));
		assertThat(employeesPage.hasNext(), is(true));
		assertThat(employeesPage.getNumberOfElements(), is(3));
		
		employeesPage = employeeRepository.findAll(employeesPage.nextPageable());
		assertThat(employeesPage.isFirst(), is(false));
		assertThat(employeesPage.hasNext(), is(true));
		assertThat(employeesPage.getNumberOfElements(), is(3));
		
		employeesPage = employeeRepository.findAll(employeesPage.nextPageable());
		assertThat(employeesPage.isFirst(), is(false));
		assertThat(employeesPage.hasNext(), is(false));
		assertThat(employeesPage.getNumberOfElements(), is(1));
		
	}
	

}
