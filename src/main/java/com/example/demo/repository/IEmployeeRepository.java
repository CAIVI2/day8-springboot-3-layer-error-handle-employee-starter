package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findEmployeesByGender(String gender);

    List<Employee> findEmployeesByGender(String gender, Pageable pageable);
}