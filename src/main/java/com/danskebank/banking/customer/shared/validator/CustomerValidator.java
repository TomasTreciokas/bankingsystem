package com.danskebank.banking.customer.shared.validator;

import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.Customer;

public interface CustomerValidator {
    void validateCustomerUniqueness(CustomerCreateRequest request);

    void validateCustomerUniqueness(Customer customer);
}
