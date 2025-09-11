package com.example.demo.controller;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeservice;

    public EmployeeController(EmployeeService employeeservice) {
        this.employeeservice = employeeservice;
    }

    @GetMapping
    public List<EmployeeResponse> getEmployees(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return EmployeeMapper.toResponses(employeeservice.getEmployees(gender, page, size));
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable int id) {
        return EmployeeMapper.toResponse(employeeservice.getEmployeeById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return EmployeeMapper.toResponse(employeeservice.createEmployee(employeeRequest));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest employeeRequest) {
        return EmployeeMapper.toResponse(employeeservice.updateEmployee(id, employeeRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employeeservice.deleteEmployee(id);
    }
}