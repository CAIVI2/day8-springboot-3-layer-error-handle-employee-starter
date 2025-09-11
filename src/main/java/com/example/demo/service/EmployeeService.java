package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidActiveEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        if (employee == null || employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("Employee age cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Employee age must be greater than 18 and less than 65");
        }
        if (employee.getAge() >= 30 && (employee.getSalary() == null || employee.getSalary() < 20000)) {
            throw new InvalidSalaryEmployeeException("Employee age greater than or equal 30 and salary must be greater than 20000");
        }
        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).toList();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeesByGender(gender);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable).stream().toList();
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeOptional.get();
    }

    public void deleteEmployee(int id) {
        Employee employee = getEmployeeById(id);
        employee.setActive(Boolean.FALSE);
        employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = getEmployeeById(id);
        if (found.getActive() == null || Boolean.FALSE.equals(found.getActive())) {
            throw new InvalidActiveEmployeeException("Employee with id: " + id + " is not active");
        }
        updatedEmployee.setId(id);
        employeeRepository.save(updatedEmployee);
        return found;
    }
}