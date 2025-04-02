package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.repository.CustomerRepository;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import com.danskebank.banking.customer.type.privates.model.CustomerPrivate;
import com.danskebank.banking.customer.type.publics.model.CustomerPublic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerPersistenceServiceImpl implements CustomerPersistenceService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerPersistenceServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public boolean existsByUniqueFields(CustomerCreateRequest request) {
        return customerRepository.existsByFullIdentity(
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                (CustomerType.INDIVIDUAL.equals(request.getCustomerType())) ? request.getLastName() : null,
                (CustomerType.INDIVIDUAL.equals(request.getCustomerType())) ? request.getNationalId() : null,
                (CustomerType.PRIVATE.equals(request.getCustomerType())) ? request.getPrivateReferenceCode() : null,
                (CustomerType.PUBLIC.equals(request.getCustomerType())) ? request.getRegistrationNumber() : null
        );
    }

    public boolean existsByUniqueFields(Customer customer) {
        return customerRepository.existsByFullIdentity(
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                (CustomerType.INDIVIDUAL.equals(customer.getCustomerType())) ? ((CustomerIndividual) customer).getLastName() : null,
                (CustomerType.INDIVIDUAL.equals(customer.getCustomerType())) ? ((CustomerIndividual) customer).getNationalId() : null,
                (CustomerType.PRIVATE.equals(customer.getCustomerType())) ? ((CustomerPrivate) customer).getPrivateReferenceCode() : null,
                (CustomerType.PUBLIC.equals(customer.getCustomerType())) ? ((CustomerPublic) customer).getRegistrationNumber() : null
        );
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
} 