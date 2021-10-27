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
import com.assignment.dto.DepartmentDto;
import com.assignment.entity.Department;
import com.assignment.service.DepartmentService;

@RestController
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	Logger log = LoggerFactory.getLogger(EmployeeController.class);

	
	//Dto to Entity
	public Department dtoToEntity(DepartmentDto departmentDto) {
		Department dep = new Department();
		dep.setName(departmentDto.getName());
		dep.setDescription(departmentDto.getDescription());
		//dep.setEmployees(departmentDto.getEmployees());
		return dep;
	} 
	
	/**
	 * Create a new department.
	 * 
	 * @param departmentDto of department entity
	 * 
	 * @return new department details in JSON
	 */
	@PostMapping(value = "/department/create")
	public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {
		log.info("Entry:: Create Department");
		try {
			return ResponseHandler.generateResponse("sucess", HttpStatus.OK,
					departmentService.createDepartment(departmentDto));
		} catch (Exception e) {
			/*
			 * ( message; details; hint; nextActions; support; )
			 */
			throw new CustomException(e.getMessage(), "Error processing the request!",
					"Exception from Create Department", "Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}
	
	/**
	 * Get the details of all the existing departments.
	 * 
	 * @return department details in JSON
	 */
	@GetMapping(value  = "/department/all")
	public ResponseEntity<?> getDepartment(){
		log.info("Entry:: Get all departments detail");
		try {
			return ResponseHandler.generateResponse("success", HttpStatus.OK, departmentService.getDepartment());
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), "Error processing the request!",
					"Exception from GET Department", "Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
			}
	}
	
	/**
	 * Get the details of specific department.
	 * 
	 * @param id Id of department
	 * @return department details in JSON
	 */
	@GetMapping("/department/details/{id}")
	public ResponseEntity<?> getDepartment(@PathVariable Long id) {
		log.info("Entry:: Get details of department by their Id");
		try {
			return ResponseHandler.generateResponse("success", HttpStatus.OK, departmentService.getDepartment(id));
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), "Error processing the request!",
					"Exception from Get Department", "Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}
	
	/**
	 * If exist, update the department 
	 * 
	 * @param id Id of department
	 * @param departmentDto DepartmentDto entry of department
	 * @return department detail in JSON
	 */
	@PutMapping("/department/update/{id}")
	public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto ){
		log.info("Entry:: Update Department");
		try {
			return ResponseHandler.generateResponse("Success", HttpStatus.OK,
					departmentService.updateDepartment(id , departmentDto));
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), "Error processing the request!",
					"Exception from Update Department", "Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
				}
	}
	
	/**
	 * If exist, delete the department 
	 * 
	 * @param id Id of department
	 */
	@DeleteMapping(value = "/department/delete/{id}")
	public ResponseEntity<?> deleteDepartment(@PathVariable Long id){
		log.info("Entry:: Delete Department");
		try {
			return ResponseHandler.generateResponse("Success", HttpStatus.OK,
					departmentService.deleteDepartment(id));
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), "Error processing the request!",
					"Exception from Delete Department", "Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
				}
	}
	
}
