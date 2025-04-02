package com.danskebank.banking.customer.shared.validator;

import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.service.CustomerPersistenceService;
import org.springframework.stereotype.Component;

@Component
public class CustomerValidatorImpl implements CustomerValidator {

    private final CustomerPersistenceService customerPersistenceService;

    public CustomerValidatorImpl(CustomerPersistenceService customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
    }

    public void validateCustomerUniqueness(CustomerCreateRequest request) {
        boolean customerExists = customerPersistenceService.existsByUniqueFields(request);

        if (customerExists) {
            throw new IllegalArgumentException("Customer with the same details already exists");
        }
    }

    @Override
    public void validateCustomerUniqueness(Customer customer) {
        boolean customerExists = customerPersistenceService.existsByUniqueFields(customer);

        if (customerExists) {
            throw new IllegalArgumentException("Customer with the same details already exists");
        }
    }
}
