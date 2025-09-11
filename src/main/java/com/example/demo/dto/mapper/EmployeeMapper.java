package com.example.demo.dto.mapper;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static List<EmployeeResponse> toResponses(List<Employee> employees) {
        return employees.stream()
                .map(emp -> toResponse(emp))
                .collect(Collectors.toList());
    }

    public static EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        BeanUtils.copyProperties(employee, employeeResponse);
        return employeeResponse;
    }

    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }
}