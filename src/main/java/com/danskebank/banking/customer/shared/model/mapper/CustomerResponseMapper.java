package com.danskebank.banking.customer.shared.model.mapper;

import com.danskebank.banking.customer.address.model.mapper.AddressMapper;
import com.danskebank.banking.customer.rest.dto.response.CustomerResponse;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import com.danskebank.banking.customer.type.privates.model.CustomerPrivate;
import com.danskebank.banking.customer.type.publics.model.CustomerPublic;

import java.util.List;
import java.util.Set;

public class CustomerResponseMapper {

    private CustomerResponseMapper() {
    }

    public static CustomerResponse mapFrom(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer instanceof CustomerIndividual ci ? ci.getLastName() : null)
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .customerType(customer.getCustomerType())
                .addresses(AddressMapper.mapFrom(customer.getAddresses()))
                .registrationNumber(customer instanceof CustomerPublic cp ? cp.getRegistrationNumber() : null)
                .privateReferenceCode(customer instanceof CustomerPrivate cp ? cp.getPrivateReferenceCode() : null)
                .addresses(AddressMapper.mapFrom(customer.getAddresses()))
                .build();
    }

    public static List<CustomerResponse> mapFrom(Set<Customer> customers) {
        return customers.stream().map(CustomerResponseMapper::mapFrom).toList();
    }
}
