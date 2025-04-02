package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.Customer;

import java.util.Optional;

public interface CustomerPersistenceService {

    Optional<Customer> findById(Long id);

    boolean existsByUniqueFields(CustomerCreateRequest request);

    boolean existsByUniqueFields(Customer customer);

    Customer save(Customer customer);
}
