package com.danskebank.banking.customer.address.model.mapper;

import com.danskebank.banking.customer.address.model.Address;
import com.danskebank.banking.customer.rest.dto.response.AddressResponse;

import java.util.List;
import java.util.Set;

public class AddressMapper {

    private AddressMapper() {
    }

    public static AddressResponse mapFrom(Address address) {
        return AddressResponse.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }

    public static List<AddressResponse> mapFrom(Set<Address> addresses) {
        return addresses.stream().map(AddressMapper::mapFrom).toList();
    }
}
