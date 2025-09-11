package com.example.demo;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("delete from employee;");
        jdbcTemplate.execute("ALTER TABLE employee AUTO_INCREMENT = 1;");
    }

    private static EmployeeRequest johnSmith() {
        return new EmployeeRequest("John Smith", 28, "MALE", 60000.0);
    }

    private static EmployeeRequest janeDoe() {
        return new EmployeeRequest("Jane Doe", 22, "FEMALE", 60000.0);
    }

    private EmployeeResponse createJohnSmith() throws Exception {
        Gson gson = new Gson();
        String John = gson.toJson(johnSmith());
        MvcResult result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(John)).andReturn();
        String responseContent = result.getResponse().getContentAsString();
        return gson.fromJson(responseContent, EmployeeResponse.class);
    }

    private Employee createJaneDoe() throws Exception {
        Gson gson = new Gson();
        String John = gson.toJson(janeDoe());
        MvcResult result = mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(John)).andReturn();
        String responseContent = result.getResponse().getContentAsString();
        return gson.fromJson(responseContent, Employee.class);
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        EmployeeResponse expect = createJohnSmith();

        mockMvc.perform(get("/employees/" + expect.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.getId()))
                .andExpect(jsonPath("$.name").value(expect.getName()))
                .andExpect(jsonPath("$.age").value(expect.getAge()))
                .andExpect(jsonPath("$.gender").value(expect.getGender()));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        EmployeeResponse expect = createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()));
    }

    @Test
    void should_create_employee() throws Exception {
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 28,
                            "gender": "MALE",
                            "salary": 60000
                        }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        EmployeeResponse expect = createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expect.getId()))
                .andExpect(jsonPath("$[0].name").value(expect.getName()))
                .andExpect(jsonPath("$[0].age").value(expect.getAge()))
                .andExpect(jsonPath("$[0].gender").value(expect.getGender()));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        int id = createJohnSmith().getId();

        mockMvc.perform(delete("/employees/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        EmployeeResponse oldEmployee = createJohnSmith();
        Gson gson = new Gson();
        Employee newEmploy = new Employee(null, "John Smith", 29, "MALE", 65000.0);
        String requestBody = gson.toJson(newEmploy);

        mockMvc.perform(put("/employees/" + oldEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(oldEmployee.getId()))
                .andExpect(jsonPath("$.age").value(newEmploy.getAge()));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_greater_than_65_or_less_than_18() throws Exception {
        Gson gson = new Gson();
        EmployeeRequest expect = johnSmith();
        expect.setAge(10);
        String john = gson.toJson(expect);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(john))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee age must be greater than 18 and less than 65"));
    }

    @Test
    void should_throw_exception_when_create_a_employee_of_greater_than_30_and_salary_less_than_20000() throws Exception {
        Gson gson = new Gson();
        EmployeeRequest expect = johnSmith();
        expect.setAge(35);
        expect.setSalary(15000.0);
        String john = gson.toJson(expect);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(john))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee age greater than or equal 30 and salary must be greater than 20000"));
    }

    @Test
    void should_set_employee_active_status_to_true_by_default_when_create_a_employee() throws Exception {
        Gson gson = new Gson();
        EmployeeRequest expect = johnSmith();
        String john = gson.toJson(expect);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(john))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void should_simply_sets_the_employee_active_status_to_false_when_delete_a_employee() throws Exception {
        EmployeeResponse expect = createJohnSmith();

        mockMvc.perform(delete("/employees/" + expect.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/" + expect.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void should_verify_whether_employee_is_active_when_update_an_employee() throws Exception {
        Gson gson = new Gson();
        EmployeeResponse employee = createJohnSmith();
        Employee updateEmployee = new Employee(null, "John Smith", 50, "MALE", 67000.0);

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updateEmployee.getAge()));
    }

    @Test
    void should_verify_whether_employee_is_not_active_when_update_an_employee() throws Exception {
        Gson gson = new Gson();
        EmployeeResponse employee = createJohnSmith();
        Employee updateEmployee = new Employee(null, "John Smith", 50, "MALE", 67000.0);

        mockMvc.perform(delete("/employees/" + employee.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(put("/employees/" + employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee with id: " + employee.getId() + " is not active"));
    }
}