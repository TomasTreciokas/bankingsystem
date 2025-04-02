package com.danskebank.banking.customer.type.publics.service;

import com.danskebank.banking.account.service.AccountService;
import com.danskebank.banking.customer.address.service.AddressService;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerUpdateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.service.CustomerPersistenceService;
import com.danskebank.banking.customer.shared.service.CustomerService;
import com.danskebank.banking.customer.shared.validator.CustomerValidator;
import com.danskebank.banking.customer.type.publics.model.CustomerPublic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
public class CustomerPublicService extends CustomerService {

    public CustomerPublicService(
            CustomerValidator customerValidator,
            CustomerPersistenceService customerPersistenceService,
            AddressService addressService,
            AccountService accountService
    ) {
        super(customerValidator, customerPersistenceService, addressService, accountService);
    }

    @Override
    protected Customer processNewCustomerCreation(CustomerCreateRequest customerCreateRequest) {
        return CustomerPublic.builder()
                .name(customerCreateRequest.getName())
                .phoneNumber(customerCreateRequest.getPhoneNumber())
                .email(customerCreateRequest.getEmail())
                .customerType(CustomerType.PUBLIC)
                .registrationNumber(customerCreateRequest.getRegistrationNumber())
                .addresses(new HashSet<>())
                .build();
    }

    @Override
    protected Customer processCustomerUpdate(Customer customer, CustomerUpdateRequest request) {
        if (!(customer instanceof CustomerPublic publicCustomer)) {
            throw new IllegalArgumentException("Customer with id " + customer.getId() + " is not a public customer");
        }

        publicCustomer.setName(request.getName());
        publicCustomer.setPhoneNumber(request.getPhoneNumber());
        publicCustomer.setEmail(request.getEmail());
        publicCustomer.setRegistrationNumber(request.getRegistrationNumber());

        return publicCustomer;
    }

    @Override
    public CustomerType getSupportedType() {
        return CustomerType.PUBLIC;
    }
}