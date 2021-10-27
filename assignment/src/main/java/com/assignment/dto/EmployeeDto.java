package com.assignment.dto;

import java.util.List;
import java.util.Set;

import com.assignment.entity.Department;

import lombok.Data;

@Data
public class EmployeeDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private List<Long> departments;

}
