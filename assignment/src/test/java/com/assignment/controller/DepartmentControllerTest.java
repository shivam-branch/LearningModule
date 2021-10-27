package com.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
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

import com.assignment.dto.DepartmentDto;
import com.assignment.dto.EmployeeDto;
import com.assignment.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private DepartmentService departmentService;

	@Test
    public void testGetDepartment() throws Exception {
    	DepartmentDto departmentDtoObj = new DepartmentDto();
    	departmentDtoObj.setName("Admin");
    	departmentDtoObj.setDescription("Administation");
    	
        Mockito.when(departmentService.getDepartment())
        .thenReturn(new ResponseEntity(departmentDtoObj, HttpStatus.OK));
        mockMvc.perform(get("/department/all")).andExpect(status().isOk());
    }
	
	@Test
	public void testCreateDepartment() throws Exception {
		DepartmentDto department = new DepartmentDto();
		department.setName("Admin");
		department.setDescription("Administartion");

		Mockito.when(departmentService.createDepartment(any(DepartmentDto.class)))
				.thenReturn(new ResponseEntity(department, HttpStatus.OK));
		mockMvc.perform(post("/department/create")
				.content(new ObjectMapper().writeValueAsString(department))
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.body.name", Matchers.equalTo("Admin")));
	}
	
	@Test
	public void testUpdateDepartment() throws Exception{
		DepartmentDto department = new DepartmentDto();
		department.setName("Admin");
		department.setDescription("Administartion");
        Mockito.when(departmentService.updateDepartment(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(DepartmentDto.class)))
        .thenReturn(new ResponseEntity(department, HttpStatus.OK));
        mockMvc.perform(put("/department/update/1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.characterEncoding("utf-8")
                .content(new ObjectMapper().writeValueAsString(department))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body.name", Matchers.equalTo("Admin")));
	}
	
	@Test
	public void testDeleteEmployee() throws Exception{
		Mockito.when(departmentService.deleteDepartment(ArgumentMatchers.anyLong()))
		.thenReturn(new ResponseEntity("department deleted", HttpStatus.OK));
        mockMvc.perform(delete("/department/delete/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.body", Matchers.equalTo("department deleted")))
                .andReturn();
	}
	
	
	
	
}
