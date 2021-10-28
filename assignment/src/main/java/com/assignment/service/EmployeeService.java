package com.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.CustomException;
import com.assignment.dto.EmployeeDto;
import com.assignment.entity.Department;
import com.assignment.entity.Employee;
import com.assignment.repository.DepartmentRepository;
import com.assignment.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DepartmentRepository departmentRepository;

	// create new employee
	public Employee createNewEmployee(Employee employee) {
		Employee employeeObj = new Employee();
		employeeObj.setFirstName(employee.getFirstName());
		employeeObj.setLastName(employee.getLastName());
		employeeObj.setMobile(employee.getMobile());
		employeeObj.setEmail(employee.getEmail());
		employeeObj.setDepartments(employee.getDepartments());
		return employeeObj;
	}

	public boolean checkAlreadyExistDepartment(Department department) {
		if (departmentRepository.findByName(department.getName()).isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Create new employee implementation. check the list of department if all
	 * department exist:
	 * 
	 * @return new employee object.
	 */

	public ResponseEntity<?> createNewEmployee(EmployeeDto employeeDto) {
		Employee employeeobj = new Employee();
		employeeobj.setFirstName(employeeDto.getFirstName());
		employeeobj.setEmail(employeeDto.getEmail());
		employeeobj.setLastName(employeeDto.getLastName());
		employeeobj.setMobile(employeeDto.getMobile());

		List<Long> departmentIdList = employeeDto.getDepartments();

		List<Department> departmentList = new ArrayList<Department>();

		for (int i = 0; i < departmentIdList.size(); i++) {
			Optional<Department> department = departmentRepository.findById(departmentIdList.get(i));
			if (!department.isPresent()) {
				return ResponseEntity.badRequest().body("the department is not present fail to create an employee");
			}
			departmentList.add(department.get());
		}

		employeeobj.setDepartments(departmentList);
		Employee saveEmployee = employeeRepository.save(employeeobj);
		if (saveEmployee == null)
			return ResponseEntity.unprocessableEntity().body("Failed creating Employee");
		else
			// convert save employee to employee dto
			return new ResponseEntity<EmployeeDto>(convertEmployeeObjectToEmployeeDto(saveEmployee), HttpStatus.OK);
	}

	/**
	 * Create new employee. Check employee is unique or not, with email.
	 * 
	 * @see createNewEmployee(EmployeeDto e)
	 * @return new employee employeeDto
	 */

	public ResponseEntity<?> createEmployee(EmployeeDto employeeDto) {
		try {
			if (employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()) {
				return ResponseEntity.badRequest().body("The email is already present fail to create new employee");
			}
			return createNewEmployee(employeeDto);
		} catch (Exception e) {
			/*
			 * ( message; details; hint; nextActions; support; )
			 */
			throw new CustomException(e.getMessage(), "Error processing the request!", "Exception from Create Employee",
					"Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}

	/*
	 * Get method for all the employee details in the database and map them with the
	 * employeeDto
	 */

	public List<EmployeeDto> getEmployee() {
		try {
			List<Employee> employeeList = employeeRepository.findAll();
			List<EmployeeDto> employeeDtoList = new ArrayList<>();
			employeeList.forEach(ele -> {
				EmployeeDto employeeDtoObj = new EmployeeDto();
				employeeDtoObj.setFirstName(ele.getFirstName());
				employeeDtoObj.setLastName(ele.getLastName());
				employeeDtoObj.setEmail(ele.getEmail());
				employeeDtoObj.setMobile(ele.getMobile());
				employeeDtoObj.setId(ele.getId());
				employeeDtoObj
						.setDepartments(ele.getDepartments().stream().map(x -> x.getId()).collect(Collectors.toList()));
				employeeDtoList.add(employeeDtoObj);
			});
			return employeeDtoList;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), "Error processing the request!", "Exception from get Employee",
					"Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");

		}
	}

	/*
	 * Get method for the specific employee details
	 * 
	 * @param id of the employee
	 * 
	 * @see convertEmployeeObjectToEmployeeDto(Employee e)
	 * 
	 * @return employeeDto of employee
	 */
	public ResponseEntity<?> getEmployeeById(Long id) {
		try {
			Optional<Employee> emp = employeeRepository.findById(id);
			if (emp.isPresent()) {
				Employee obj = emp.get();
				EmployeeDto employee = convertEmployeeObjectToEmployeeDto(obj);
				return new ResponseEntity<EmployeeDto>(employee, HttpStatus.OK);
			} else
				return ResponseEntity.badRequest().body("The employee is not Present");
		} catch (Exception e) {
			/*
			 * ( message; details; hint; nextActions; support; )
			 */
			throw new CustomException(e.getMessage(), "Error processing the request!", "Exception from get Employee",
					"Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}

	/*
	 * map the given employee into there employee dto.
	 * 
	 * @param Employee object
	 * 
	 * @return EmployeeDto
	 */
	public EmployeeDto convertEmployeeObjectToEmployeeDto(Employee emp) {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setFirstName(emp.getFirstName());
		employeeDto.setLastName(emp.getLastName());
		employeeDto.setEmail(emp.getEmail());
		employeeDto.setMobile(emp.getMobile());
		employeeDto.setId(emp.getId());
		employeeDto.setDepartments(emp.getDepartments().stream().map(e -> e.getId()).collect(Collectors.toList()));
		return employeeDto;
	}

	// update Employee
	@Transactional
	public ResponseEntity<Object> updateEmployee(Long id, Employee employee) {

		if (employeeRepository.findById(id).isPresent()) {
			Employee newEmployee = employeeRepository.findById(id).get();
			newEmployee.setFirstName(employee.getFirstName());
			newEmployee.setLastName(employee.getLastName());
			newEmployee.setEmail(employee.getEmail());
			newEmployee.setMobile(employee.getEmail());
			newEmployee.setDepartments(employee.getDepartments());
			employeeRepository.save(newEmployee);
			return ResponseEntity.accepted().body("Employee update successfully");
		} else {
			return ResponseEntity.unprocessableEntity().body("Cannot find the employee ");
		}

	}

	public ResponseEntity<?> updateEmployee(Long id, EmployeeDto employeeDto) {
		try {
			Optional<Employee> emp = employeeRepository.findById(id);
			if (emp.isPresent()) {
				Employee newEmployee = emp.get();
				newEmployee.setFirstName(
						employeeDto.getFirstName() != null ? employeeDto.getFirstName() : newEmployee.getFirstName());
				newEmployee.setLastName(
						employeeDto.getLastName() != null ? employeeDto.getLastName() : newEmployee.getLastName());
				newEmployee.setEmail(employeeDto.getEmail() != null ? employeeDto.getEmail() : newEmployee.getEmail());
				newEmployee
						.setMobile(employeeDto.getMobile() != null ? employeeDto.getMobile() : newEmployee.getMobile());

				List<Long> departmentIdList = employeeDto.getDepartments();
				List<Department> departmentList = new ArrayList<Department>();

				for (int i = 0; i < departmentIdList.size(); i++) {
					Optional<Department> department = departmentRepository.findById(departmentIdList.get(i));
					if (!department.isPresent()) {
						return ResponseEntity.badRequest()
								.body("the department is not present fail to update an employee");
					}
					departmentList.add(department.get());
				}

				newEmployee.setDepartments(departmentList);

				employeeRepository.save(newEmployee);
				return getEmployeeById(id);
			} else {
				return ResponseEntity.unprocessableEntity().body("Cannot find the employee ");
			}
		} catch (Exception e) {
			/*
			 * ( message; details; hint; nextActions; support; )
			 */
			throw new CustomException(e.getMessage(), "Error processing the request!", "Exception from Update Employee",
					"Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}

	public ResponseEntity<?> deleteEmployee(Long id) {
		try {
			Optional<Employee> emp = employeeRepository.findById(id);
			if (emp.isPresent()) {
				employeeRepository.deleteById(emp.get().getId());
				return ResponseEntity.ok("Employee deleted");
			} else
				return ResponseEntity.unprocessableEntity().body("Employee not found ");
		} catch (Exception e) {
			/*
			 * ( message; details; hint; nextActions; support; )
			 */
			throw new CustomException(e.getMessage(), "Error processing the request!", "Exception from delete Employee",
					"Ask your friends for access at https://www.unthinkable.co/",
					"Reach out to https://www.unthinkable.co/web-mobile-application-development/ for more help");
		}
	}

}
