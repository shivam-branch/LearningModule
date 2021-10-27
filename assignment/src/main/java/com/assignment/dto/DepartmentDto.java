package com.assignment.dto;

import java.util.List;


import lombok.Data;

@Data
public class DepartmentDto {

	private Long id;
	private String name;
	private String description;
	private List<Long> employees;
}
