package com.danskebank.banking.customer.rest.dto.response;

import lombok.Builder;

@Builder
public record AddressResponse(
        Long id,
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {
}