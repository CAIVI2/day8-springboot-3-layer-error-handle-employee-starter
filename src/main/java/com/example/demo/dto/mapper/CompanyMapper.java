package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static List<CompanyResponse> toResponses(List<Company> companies) {
        return companies.stream()
                .map(comp -> toResponse(comp))
                .collect(Collectors.toList());
    }

    public static CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        return companyResponse;
    }

    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        return company;
    }
}