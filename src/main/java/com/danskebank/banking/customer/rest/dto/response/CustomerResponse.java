package com.danskebank.banking.customer.rest.dto.response;

import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CustomerResponse(
        Long id,
        CustomerType customerType,
        String name,
        String lastName,
        String phoneNumber,
        String email,
        LocalDate dateOfBirth,
        String registrationNumber,
        String privateReferenceCode,
        List<AddressResponse> addresses
) {
}