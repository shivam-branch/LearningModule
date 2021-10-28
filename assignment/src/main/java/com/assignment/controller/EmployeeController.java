package com.assignment.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import com.assignment.CustomException;
import com.assignment.ResponseHandler;
import com.assignment.dto.EmployeeDto;
import com.assignment.service.EmployeeService;


@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	Logger log = LoggerFactory.getLogger(EmployeeController.class);
	
	/**
	 * Create a new employee.
	 * 
	 * @param employeeDto of an employee
	 * 
	 * @return new employee details in JSON
	 */
	@PostMapping(value = "/employee/create")
	public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
		log.info("Entry:: Create Employee");
		return ResponseHandler.generateResponse("success", HttpStatus.OK, employeeService.createEmployee(employeeDto));
	}
	
	/**
	 * Get the details of all the existing employee.
	 * 
	 * @return employee details in JSON
	 */
	@GetMapping(value = "/employee/all")
	public ResponseEntity<?> getEmployee() {
		log.info("Entry::  Get all Employees details");
		return ResponseHandler.generateResponse("success", HttpStatus.OK, employeeService.getEmployee());
	}
	
	/**
	 * Get the details of specific employee.
	 * 
	 * @param id Id of employee
	 * @return employee detail in JSON
	 */
	@GetMapping(value  = "/employee/details/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
		log.info("Entry:: Get details of employee by their Id");
		return ResponseHandler.generateResponse("Success", HttpStatus.OK, employeeService.getEmployeeById(id));
	}
	
	/**
	 * If exist, update the employee 
	 * 
	 * @param id Id of employee
	 * @param employeeDto EmployeeDto entry of an employee
	 * @return employee detail in JSON
	 */
	@PutMapping(value = "/employee/update/{id}")
	public ResponseEntity<Object> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
		log.info("Entry:: Update Employee");
			return ResponseHandler.generateResponse("Success", HttpStatus.OK,
					employeeService.updateEmployee(id, employeeDto));
	}
	
	/**
	 * If exist, delete the employee 
	 * 
	 * @param id Id of employee
	 */
	@DeleteMapping(value = "/employee/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
		log.info("Entry:: Delete Employee");
			return ResponseHandler.generateResponse("Success", HttpStatus.OK,
					employeeService.deleteEmployee(id));
	}
}
