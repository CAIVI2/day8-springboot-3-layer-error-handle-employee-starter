package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public Employee createEmployee(Employee employee) {
        employee.setId(employees.size() + 1);
        employees.add(employee);
        return employee;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void empty() {
        employees.clear();
    }

    public Employee getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteEmployee(Employee employee) {
        employees.remove(employee);
    }

    public Employee updateEmployee(Employee oldEmployee, Employee updatedEmployee) {
        oldEmployee.setName(updatedEmployee.getName());
        oldEmployee.setAge(updatedEmployee.getAge());
        oldEmployee.setGender(updatedEmployee.getGender());
        oldEmployee.setSalary(updatedEmployee.getSalary());
        return oldEmployee;
    }
}
