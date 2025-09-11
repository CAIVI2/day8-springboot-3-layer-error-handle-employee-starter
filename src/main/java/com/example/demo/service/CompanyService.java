package com.example.demo.service;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.mapper.CompanyMapper;
import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(CompanyRequest companyRequest) {
        return companyRepository.save(CompanyMapper.toEntity(companyRequest));
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page - 1, size);
            return companyRepository.findAll(pageable).toList();
        }
    }

    public Company getCompanyById(int id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company.orElse(null);
    }

    public void deleteCompany(int id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    public Company updateCompany(int id, CompanyRequest companyRequest) {
        Company company = getCompanyById(id);
        Company entity = CompanyMapper.toEntity(companyRequest);
        entity.setId(company.getId());
        return companyRepository.save(entity);
    }
}