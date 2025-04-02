package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.account.service.AccountService;
import com.danskebank.banking.customer.address.service.AddressService;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerUpdateRequest;
import com.danskebank.banking.customer.rest.dto.response.CustomerResponse;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.model.mapper.CustomerResponseMapper;
import com.danskebank.banking.customer.shared.validator.CustomerValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class CustomerService {

    protected final CustomerValidator customerValidator;
    protected final CustomerPersistenceService customerPersistenceService;
    protected final AddressService addressService;
    protected final AccountService accountService;

    public CustomerService(
            CustomerValidator customerValidator,
            CustomerPersistenceService customerPersistenceService,
            AddressService addressService,
            AccountService accountService
    ) {
        this.customerValidator = customerValidator;
        this.customerPersistenceService = customerPersistenceService;
        this.addressService = addressService;
        this.accountService = accountService;
    }

    protected abstract Customer processNewCustomerCreation(CustomerCreateRequest customerCreateRequest);

    protected abstract Customer processCustomerUpdate(Customer existingCustomer, CustomerUpdateRequest newCustomer);

    protected abstract CustomerType getSupportedType();

    @Transactional
    public Customer createCustomer(CustomerCreateRequest customerCreateRequest) {
        log.info("Creating customer {} of type {}", customerCreateRequest.getName(), customerCreateRequest.getCustomerType());

        customerValidator.validateCustomerUniqueness(customerCreateRequest);

        Customer newCustomer = processNewCustomerCreation(customerCreateRequest);
        addressService.addAddresses(newCustomer, customerCreateRequest.getAddresses());
        accountService.linkOwnerToAccounts(newCustomer, customerCreateRequest.getAccountIds());

        Customer savedCustomer = customerPersistenceService.save(newCustomer);

        log.info("Successfully created customer {} of type {}", customerCreateRequest.getName(), customerCreateRequest.getCustomerType());

        return savedCustomer;
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) {
        log.info("Updating public customer with id: {}", id);

        Customer customer = customerPersistenceService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        Customer updatedCustomer = processCustomerUpdate(customer, customerUpdateRequest);
        addressService.syncAddresses(updatedCustomer, customerUpdateRequest.getAddresses());
        accountService.syncAccountOwners(updatedCustomer, customerUpdateRequest.getAccountIds());

        customerValidator.validateCustomerUniqueness(updatedCustomer);


        Customer savedCustomer = customerPersistenceService.save(updatedCustomer);

        log.info("Finished updating customer with id: {}", id);

        return CustomerResponseMapper.mapFrom(savedCustomer);
    }
}