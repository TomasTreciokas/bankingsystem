package com.danskebank.banking.account.rest.dto.response;

import com.danskebank.banking.customer.rest.dto.response.CustomerResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record AccountResponse(
        Long id,
        String accountNumber,
        BigDecimal balance,
        int numberOfOwners,
        List<CustomerResponse> owners
) {
}