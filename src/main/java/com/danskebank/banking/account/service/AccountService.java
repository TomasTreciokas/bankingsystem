package com.danskebank.banking.account.service;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.customer.shared.model.Customer;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest);

    Account getAccount(Long id);

    void linkOwnerToAccounts(Customer customer, List<Long> accountIds);

    void syncAccountOwners(Customer customer, List<Long> accountIds);
}