package com.danskebank.banking.customer.type.individuals.service;

import com.danskebank.banking.account.service.AccountService;
import com.danskebank.banking.customer.address.service.AddressService;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerUpdateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.service.CustomerPersistenceService;
import com.danskebank.banking.customer.shared.service.CustomerService;
import com.danskebank.banking.customer.shared.validator.CustomerValidator;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerIndividualService extends CustomerService {

    public CustomerIndividualService(
            CustomerValidator customerValidator,
            CustomerPersistenceService customerPersistenceService,
            AddressService addressService,
            AccountService accountService
    ) {
        super(customerValidator, customerPersistenceService, addressService, accountService);
    }

    @Override
    protected Customer processNewCustomerCreation(CustomerCreateRequest customerCreateRequest) {
        return CustomerIndividual.builder()
                .name(customerCreateRequest.getName())
                .lastName(customerCreateRequest.getLastName())
                .phoneNumber(customerCreateRequest.getPhoneNumber())
                .email(customerCreateRequest.getEmail())
                .customerType(CustomerType.INDIVIDUAL)
                .nationalId(customerCreateRequest.getNationalId())
                .dateOfBirth(customerCreateRequest.getDateOfBirth())
                .build();
    }

    protected Customer processCustomerUpdate(Customer customer, CustomerUpdateRequest request) {
        if (!(customer instanceof CustomerIndividual individualCustomer)) {
            throw new IllegalArgumentException("Customer with id " + customer.getId() + " is not a public customer");
        }

        individualCustomer.setName(request.getName());
        individualCustomer.setLastName(request.getLastName());
        individualCustomer.setPhoneNumber(request.getPhoneNumber());
        individualCustomer.setEmail(request.getEmail());
        individualCustomer.setDateOfBirth(request.getDateOfBirth());

        return individualCustomer;
    }

    @Override
    public CustomerType getSupportedType() {
        return CustomerType.INDIVIDUAL;
    }
}
