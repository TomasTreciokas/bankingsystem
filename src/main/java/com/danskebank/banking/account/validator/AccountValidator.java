package com.danskebank.banking.account.validator;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.customer.shared.model.Customer;

import java.util.List;
import java.util.Set;

public interface AccountValidator {

    void validateAccountOwnerDuplicate(Set<Customer> owners, Customer customerToAdd);

    void validateIfAllAccountsMatch(List<Long> accountIds, List<Account> accountsToSync);
}
