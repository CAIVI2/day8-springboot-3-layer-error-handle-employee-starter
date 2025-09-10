package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Employee createEmployee(Employee employee) {
        if (employee == null || employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("Employee age cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException( "Employee age must be greater than 18 and less than 65");
        }
        employee.setActive(true);
        return employeeRepository.createEmployee(employee);
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        List<Employee> employees = employeeRepository.getEmployees();
        Stream<Employee> stream = employees.stream();
        if (gender != null) {
            stream = stream.filter(employee -> employee.getGender().compareToIgnoreCase(gender) == 0);
        }
        if (page != null && size != null) {
            stream = stream.skip((long) (page - 1) * size).limit(size);
        }
        return stream.toList();
    }

    public void empty() {
        employeeRepository.empty();
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee;
    }

    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employee.setActive(Boolean.FALSE);
        employeeRepository.updateEmployee(employee, employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = employeeRepository.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        Employee employee = employeeRepository.updateEmployee(found, updatedEmployee);
        return employee;
    }
}