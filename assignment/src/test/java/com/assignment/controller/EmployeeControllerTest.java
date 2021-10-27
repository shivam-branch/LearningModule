package com.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;

import com.assignment.dto.EmployeeDto;
import com.assignment.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private EmployeeService employeeService;
    
    
 
   @Test
    public void testGetEmployee() throws Exception {
    	EmployeeDto employeeDtoObj = new EmployeeDto();
		employeeDtoObj.setFirstName("Sohan");
		employeeDtoObj.setLastName("Kumar");
		employeeDtoObj.setEmail("353@gjk.co");
		employeeDtoObj.setMobile("4235453");
		
		List<EmployeeDto> employeeDtoList = new ArrayList();
        Mockito.when(employeeService.getEmployee()).thenReturn(employeeDtoList);
        mockMvc.perform(get("/employee/all")).andExpect(status().isOk());
        
    }
    
	@Test
	public void testCreateEmployee() throws Exception {
		EmployeeDto emp = new EmployeeDto();
		List<Long> depList = new ArrayList<>();
		emp.setFirstName("Sohan");
		emp.setLastName("Kumar");
		emp.setMobile("35387587");
		emp.setEmail("638398@gmail.com");
		emp.setDepartments(depList);

		Mockito.when(employeeService.createEmployee(any(EmployeeDto.class)))
				.thenReturn(new ResponseEntity(emp, HttpStatus.OK));
		mockMvc.perform(post("/employee/create")
				.content(new ObjectMapper().writeValueAsString(emp))
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.body.firstName", Matchers.equalTo("Sohan")));
	}
	
	@Test
	public void testUpdateEmployee() throws Exception{
		EmployeeDto employee = new EmployeeDto();
        employee.setEmail("sagar@Gmail.co");
        employee.setFirstName("Sagar");
        Mockito.when(employeeService.updateEmployee(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(EmployeeDto.class)))
        .thenReturn(new ResponseEntity(employee, HttpStatus.OK));
        mockMvc.perform(put("/employee/update/1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(employee))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body.firstName", Matchers.equalTo("Sagar")));
   
	}
	
	@Test
	public void testDeleteEmployee() throws Exception{
		Mockito.when(employeeService.deleteEmployee(ArgumentMatchers.anyLong()))
		.thenReturn(new ResponseEntity("employee deleted", HttpStatus.OK));
        mockMvc.perform(delete("/employee/delete/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body", Matchers.equalTo("employee deleted")))
                .andReturn();
	}
	
	
}



