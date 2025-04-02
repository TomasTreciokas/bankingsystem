package com.danskebank.banking.customer.type.privates.service;

import com.danskebank.banking.account.service.AccountService;
import com.danskebank.banking.customer.address.service.AddressService;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerUpdateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.service.CustomerPersistenceService;
import com.danskebank.banking.customer.shared.service.CustomerService;
import com.danskebank.banking.customer.shared.validator.CustomerValidator;
import com.danskebank.banking.customer.type.privates.model.CustomerPrivate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
public class CustomerPrivateService extends CustomerService {

    public CustomerPrivateService(
            CustomerValidator customerValidator,
            CustomerPersistenceService customerPersistenceService,
            AddressService addressService,
            AccountService accountService
    ) {
        super(customerValidator, customerPersistenceService, addressService, accountService);
    }

    @Override
    protected Customer processNewCustomerCreation(CustomerCreateRequest customerCreateRequest) {
        return CustomerPrivate.builder()
                .name(customerCreateRequest.getName())
                .phoneNumber(customerCreateRequest.getPhoneNumber())
                .email(customerCreateRequest.getEmail())
                .customerType(CustomerType.PRIVATE)
                .privateReferenceCode(customerCreateRequest.getPrivateReferenceCode())
                .addresses(new HashSet<>())
                .build();
    }

    @Override
    protected Customer processCustomerUpdate(Customer existingCustomer, CustomerUpdateRequest newCustomer) {
        if (!(existingCustomer instanceof CustomerPrivate privateCustomer)) {
            throw new IllegalArgumentException("Customer with id " + existingCustomer.getId() + " is not a private customer");
        }

        privateCustomer.setName(newCustomer.getName());
        privateCustomer.setPhoneNumber(newCustomer.getPhoneNumber());
        privateCustomer.setEmail(newCustomer.getEmail());
        privateCustomer.setPrivateReferenceCode(newCustomer.getPrivateReferenceCode());

        return privateCustomer;
    }

    @Override
    public CustomerType getSupportedType() {
        return CustomerType.PRIVATE;
    }
}
