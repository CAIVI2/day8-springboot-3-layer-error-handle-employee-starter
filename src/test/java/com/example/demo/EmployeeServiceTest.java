package com.example.demo;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidActiveEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    void should_throw_exception_when_create_a_employee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Young Guy", 20, "MALE", 50000.0);

        employeeService.createEmployee(employeeRequest);

        verify(employeeRepository).save(argThat(e -> e.getName().equals(employeeRequest.getName())
                && e.getAge().equals(employeeRequest.getAge())
                && e.getGender().equals(employeeRequest.getGender())
                && e.getSalary().equals(employeeRequest.getSalary())
                && e.getActive().equals(Boolean.TRUE)));
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_greater_than_65_or_less_then_18() {
        EmployeeRequest employeeRequest = new EmployeeRequest( "Old Guy", 70, "MALE", 50000.0);

        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employeeRequest));
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_age_greater_than_or_equal_30_and_salary_must_not_less_than_20000() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Old Guy", 60, "MALE", 10000.0);

        assertThrows(InvalidSalaryEmployeeException.class, () -> employeeService.createEmployee(employeeRequest));
    }

    @Test
    void should_set_employee_active_status_to_true_by_default_when_create_a_employee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Young Guy", 22, "MALE", 1000.0);

        Employee expect = employeeService.createEmployee(employeeRequest);

        verify(employeeRepository).save(argThat(Employee::getActive));
    }

    @Test
    void should_simply_sets_the_employee_active_status_to_false_when_delete_a_employee() {
        Employee employee = new Employee(1, "Young Guy", 22, "MALE", 1000.0, Boolean.TRUE);
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employee.getId());

        verify(employeeRepository).save(argThat(e -> e.getActive().equals(Boolean.FALSE)));
    }

    @Test
    void should_verify_whether_employee_is_active_when_update_an_employee() {
        Employee employee = new Employee(1, "Young Guy", 22, "MALE", 1000.0, Boolean.TRUE);
        EmployeeRequest employeeRequest = new EmployeeRequest("Old Guy", 52, "MALE", 50000.0, Boolean.TRUE);
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.updateEmployee(employee.getId(), employeeRequest);

        verify(employeeRepository).save(argThat(e ->
                e.getName().equals(employeeRequest.getName())
                        && e.getAge().equals(employeeRequest.getAge())
                        && e.getSalary().equals(employeeRequest.getSalary())
                        && e.getActive().equals(Boolean.TRUE)));
    }

    @Test
    void should_verify_whether_employee_is_not_active_when_update_an_employee() {
        Employee employee = new Employee(1, "Young Guy", 22, "MALE", 1000.0);
        employee.setActive(Boolean.FALSE);
        EmployeeRequest employeeRequest = new EmployeeRequest("Old Guy", 52, "MALE", 50000.0);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        assertThrows(InvalidActiveEmployeeException.class, () -> employeeService.updateEmployee(employee.getId(), employeeRequest));
    }
}