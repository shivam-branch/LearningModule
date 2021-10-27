package com.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.dto.DepartmentDto;
import com.assignment.entity.Department;
import com.assignment.entity.Employee;
import com.assignment.repository.DepartmentRepository;
import com.assignment.repository.EmployeeRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	public Department createNewDepartment(Department department) {
		Department departmentObj = new Department();
		departmentObj.setName(department.getName());
		departmentObj.setDescription(department.getDescription());
//		List<Department> departmentList = new ArrayList<>();
//		departmentList.add(departmentObj);
//		for (int i = 0; i < department.getEmployees().size(); i++) {
//			if (!employeeRepository.findByEmail(department.getEmployees().get(i).getEmail()).isPresent()) {
//				Employee newEmployee = department.getEmployees().get(i);
//				newEmployee.setDepartments(departmentList);
//				 employeeRepository.save(newEmployee);
//			}
//		}

		 departmentObj.setEmployees(department.getEmployees());
		return departmentObj;
	}

	public ResponseEntity<Object> createDepartment(Department department) {
		if (departmentRepository.findByName(department.getName()).isPresent()) {
			return ResponseEntity.badRequest()
					.body("The same department is already present fail to create new department");
		}
		Department departmentobj = createNewDepartment(department);
		 Department saveDepartment = departmentRepository.save(departmentobj);
		return ResponseEntity.ok("Department Created Successfully");
	}
	
	public void updateDepartmentInExistingUser(Employee e, Department d) {
		List<Department> depList = new ArrayList<>();
		depList.add(d);
		e.getDepartments().forEach(x-> {
			depList.add(x);
		});
		e.setDepartments(depList);
		employeeRepository.save(e);
	}
	
	public ResponseEntity<?> createNewDepartment(DepartmentDto departmentDto){
		Department departmentObj = new Department();
		departmentObj.setName(departmentDto.getName());
		departmentObj.setDescription(departmentDto.getDescription());
		

		/*
		 * check all employee exist in employee table 
		 * if employeeIdslist size = 0 then make
		 * department if employee doesn't exist return Bad request
		 */		
		List<Long> employeeIdsList =  departmentDto.getEmployees();
		for(int i = 0; i<employeeIdsList.size();i++) {
			if(!employeeRepository.findById(employeeIdsList.get(i)).isPresent()) {
			return ResponseEntity.badRequest().body("The Employee Id "+ employeeIdsList.get(i)+ " is not present");	
			}
		}
	
		Department saveDepartment = departmentRepository.save(departmentObj);	
		//check for employee
		List<Long> employeeIdList =  departmentDto.getEmployees();
		List<Employee> employeeList= new ArrayList<>();
		for(int i = 0; i < employeeIdList.size(); i++) {
			Optional<Employee> existingEmp = employeeRepository.findById(employeeIdList.get(i));
			updateDepartmentInExistingUser(existingEmp.get(), saveDepartment);
			employeeList.add(existingEmp.get());	

		}
		departmentObj.setEmployees(employeeList);
		//Department saveDepartment = departmentRepository.save(departmentObj);
		if(saveDepartment == null)
			return ResponseEntity.unprocessableEntity().body("Failed creating Employee");
		else
		    return new ResponseEntity<DepartmentDto>(convertDepartmnetObjToDepartmentDto(saveDepartment), HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<?> createDepartment(DepartmentDto departmentDto) {
		if(departmentRepository.findByName(departmentDto.getName()).isPresent()) {
			return ResponseEntity.badRequest()
					.body("The department is already Present");
		}
	 return createNewDepartment(departmentDto);

	}

	/*
	 * for get Department request.
	 * Convert all the department Object into the departmentDto 
	 */	
	public ResponseEntity<DepartmentDto> getDepartment() {
		List<Department> departmentList = departmentRepository.findAll();
		List<DepartmentDto> departmentDtoList = new ArrayList<>();
		departmentList.forEach(x -> {
			DepartmentDto departmentdtoObj = new DepartmentDto();
			departmentdtoObj.setId(x.getId());
			departmentdtoObj.setName(x.getName());
			departmentdtoObj.setDescription(x.getDescription());
			departmentdtoObj
					.setEmployees(x.getEmployees().stream().map(ele -> ele.getId()).collect(Collectors.toList()));
			departmentDtoList.add(departmentdtoObj);
		});
		return new ResponseEntity(departmentDtoList, HttpStatus.OK);
	}
	
	public ResponseEntity<?> getDepartment(Long id) {
		Optional<Department> requiredDepartment = departmentRepository.findById(id);
		if (requiredDepartment.isPresent()) {
			Department obj = requiredDepartment.get();
			DepartmentDto dep =  convertDepartmnetObjToDepartmentDto(obj);
			return new ResponseEntity<DepartmentDto>(dep, HttpStatus.OK);
		}
			
		else
			return ResponseEntity.badRequest()
					.body("The department is not Present");
	}
	
	public DepartmentDto convertDepartmnetObjToDepartmentDto(Department obj) {
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setId(obj.getId());
		departmentDto.setName(obj.getName());
		departmentDto.setDescription(obj.getDescription());
		departmentDto.setEmployees(obj.getEmployees().stream()
				.map(element -> element.getId())
				.collect(Collectors.toList()));
		return departmentDto;
	}
	
	
	/*
	 * Update department
	 */
	public ResponseEntity<?> updateDepartment(Long id, Department department) {
		if (departmentRepository.findById(id).isPresent()) {
			Department newDepartment = departmentRepository.findById(id).get();
			newDepartment.setName(department.getName());
			newDepartment.setDescription(department.getDescription());
			Department saveDepartment = departmentRepository.save(newDepartment);
			if (departmentRepository.findById(saveDepartment.getId()).isPresent())
				// return ResponseEntity.accepted().body("Department update successfully");
				return new ResponseEntity<Department>(saveDepartment, HttpStatus.OK);
			else
				return ResponseEntity.badRequest().body("Failed to update Department");

		} else
			return ResponseEntity.unprocessableEntity().body("Specified Department not found");
	}
	
	public ResponseEntity<?> updateDepartment(Long id, DepartmentDto departmentDto) {
		Optional<Department> dep = departmentRepository.findById(id);
		if (dep.isPresent()) {
			Department newDepartment = dep.get();
			newDepartment.setName(departmentDto.getName() != null ? departmentDto.getName() : newDepartment.getName());
			newDepartment.setDescription(departmentDto.getDescription() != null ? departmentDto.getDescription()
					: newDepartment.getDescription());

			Department saveDepartment = departmentRepository.save(newDepartment);

			// check for employee
			List<Long> employeeIdList = departmentDto.getEmployees();
			List<Employee> employeeList = new ArrayList<>();
			for (int i = 0; i < employeeIdList.size(); i++) {
				Optional<Employee> existingEmp = employeeRepository.findById(employeeIdList.get(i));
				if (!existingEmp.isPresent()) {
					return ResponseEntity.badRequest().body("the employee is not present fail to create an department");
				}
				updateDepartmentInExistingUser(existingEmp.get(), saveDepartment);
				employeeList.add(existingEmp.get());

			}
			
			return getDepartment(id);
		} else {
			return ResponseEntity.unprocessableEntity().body("Cannot find the department ");
		}
	}
	
	@Transactional
	public ResponseEntity<?> deleteDepartment(Long id) {
		Optional<Department> dep = departmentRepository.findById(id);
		if (dep.isPresent()) {
			if (dep.get().getEmployees().size() == 0) {
				departmentRepository.deleteById(dep.get().getId());
				return ResponseEntity.ok("Department deleted successfully");
			} else
				return ResponseEntity.unprocessableEntity()
						.body("Failed to delete,  Please delete the employee associated with this department");
		} else
			return ResponseEntity.unprocessableEntity().body("Department of this ID is not present");
	}
}
