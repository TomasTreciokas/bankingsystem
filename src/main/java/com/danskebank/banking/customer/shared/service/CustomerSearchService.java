package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.rest.dto.response.CustomerResponse;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.mapper.CustomerResponseMapper;
import com.danskebank.banking.customer.shared.repository.CustomerRepository;
import com.danskebank.banking.general.dto.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerSearchService {

    private final CustomerRepository customerRepository;

    public CustomerSearchService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public PageableResponse<CustomerResponse> searchCustomers(CustomerSearchRequest searchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Customer> customerPage = customerRepository.findAll(CustomerSpecification.build(searchCriteria), pageable);

        List<CustomerResponse> customerResponses = customerPage.getContent().stream()
                .map(CustomerResponseMapper::mapFrom)
                .collect(Collectors.toList());

        return new PageableResponse<>(
                customerResponses,
                customerPage.getTotalPages(),
                customerPage.getTotalElements(),
                customerPage.getNumber(),
                customerPage.getSize()
        );
    }
}
