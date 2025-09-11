package com.example.demo.dto;

import java.util.List;

public class CompanyResponse {
    private Integer id;
    private String name;
    private List<EmployeeResponse> employees;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyResponse() {
    }

    public CompanyResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeResponse> employees) {
        this.employees = employees;
    }
}