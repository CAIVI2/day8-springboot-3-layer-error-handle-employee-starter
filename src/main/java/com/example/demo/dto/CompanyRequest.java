package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CompanyRequest {
    @NotNull(message = "Name cannot be null")
    private String name;
    private List<EmployeeRequest> employees;

    public CompanyRequest() {
    }

    public CompanyRequest(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<EmployeeRequest> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeRequest> employees) {
        this.employees = employees;
    }
}
