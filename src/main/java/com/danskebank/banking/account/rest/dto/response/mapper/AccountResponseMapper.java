package com.danskebank.banking.account.rest.dto.response.mapper;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.rest.dto.response.AccountResponse;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.mapper.CustomerResponseMapper;

import java.util.Set;

public class AccountResponseMapper {

    private AccountResponseMapper() {
    }

    public static AccountResponse mapFrom(Account account) {
        Set<Customer> accountOwners = account.getOwners();

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .owners(CustomerResponseMapper.mapFrom(account.getOwners()))
                .numberOfOwners(accountOwners.size())
                .build();
    }
}
