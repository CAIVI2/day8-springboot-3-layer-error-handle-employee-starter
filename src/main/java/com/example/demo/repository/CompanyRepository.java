package com.example.demo.repository;

import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    private final List<Company> companies = new ArrayList<>();

    public Company createCompany(Company company) {
        company.setId(1);
        companies.add(company);
        return company;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public Company getCompanyById(int id) {
        return companies.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public void deleteCompany(Company company) {
        companies.remove(company);
    }

    public Company updateCompany(Company company, Company updatedCompany) {
        company.setName(updatedCompany.getName());
        return company;
    }

    public void empty() {
        companies.clear();
    }
}
