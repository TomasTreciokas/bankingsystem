package com.danskebank.banking.account.validator;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AccountValidatorImpl implements AccountValidator {

    public void validateAccountOwnerDuplicate(Set<Customer> owners, Customer customerToAdd) {
        boolean alreadyLinked = owners.stream().anyMatch(owner ->
                Objects.equals(owner.getName(), customerToAdd.getName()) &&
                        (!(customerToAdd instanceof CustomerIndividual cia) || !(owner instanceof CustomerIndividual cio) || Objects.equals(cio.getLastName(), cia.getLastName())) &&
                        Objects.equals(owner.getPhoneNumber(), customerToAdd.getPhoneNumber()) &&
                        Objects.equals(owner.getEmail(), customerToAdd.getEmail())
        );

        if (alreadyLinked) {
            throw new IllegalStateException("Customer already linked to this account.");
        }
    }

    public void validateIfAllAccountsMatch(List<Long> accountIds, List<Account> accountsToSync) {
        if (accountIds == null || accountIds.isEmpty()) {
            return;
        }

        if (accountsToSync.size() != accountIds.size()) {
            Set<Long> foundIds = accountsToSync.stream()
                    .map(Account::getId)
                    .collect(Collectors.toSet());

            List<Long> notFoundIds = accountIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new IllegalArgumentException("Some accounts were not found: " + notFoundIds);
        }
    }
}
