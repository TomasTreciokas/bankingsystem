package com.danskebank.banking.customer.shared.service;

import com.danskebank.banking.customer.shared.model.enums.CustomerType;

import java.time.LocalDate;

public record CustomerSearchRequest(
        String searchCriteria,
        CustomerType customerType,
        String nationalId,
        LocalDate dateOfBirth,
        String registrationNumber,
        String privateReferenceCode
) {
}
