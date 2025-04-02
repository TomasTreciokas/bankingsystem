package com.danskebank.banking.account.service;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.repository.AccountRepository;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.account.validator.AccountValidator;
import com.danskebank.banking.customer.shared.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountValidator accountValidator;

    @Autowired
    public AccountServiceImpl(
            AccountRepository accountRepository,
            AccountValidator accountValidator
    ) {
        this.accountRepository = accountRepository;
        this.accountValidator = accountValidator;
    }

    @Override
    @Transactional
    public Account createAccount(AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null) {
            throw new IllegalArgumentException("Account number cannot be null");
        }

        if (accountRepository.existsByAccountNumber(accountRequest.getAccountNumber())) {
            throw new DataIntegrityViolationException("Account with this number already exists");
        }

        Account account = Account.builder()
                .accountNumber(accountRequest.getAccountNumber())
                .balance(accountRequest.getBalance())
                .build();

        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
    }

    @Transactional
    public void linkOwnerToAccounts(Customer newOwner, List<Long> accountIds) {
        List<Account> account = accountRepository.findAllById(accountIds);

        accountValidator.validateIfAllAccountsMatch(accountIds, account);

        account.forEach(acc -> {
            accountValidator.validateAccountOwnerDuplicate(acc.getOwners(), newOwner);
            newOwner.getAccounts().add(acc);
            acc.getOwners().add(newOwner);
        });
    }

    @Transactional
    public void syncAccountOwners(Customer customer, List<Long> accountIds) {
        List<Long> safeAccountIds = accountIds == null ? Collections.emptyList() : accountIds;
        List<Account> accountsToSync = safeAccountIds.isEmpty() ?
                Collections.emptyList() :
                accountRepository.findAllById(safeAccountIds);

        accountValidator.validateIfAllAccountsMatch(safeAccountIds, accountsToSync);

        Set<Account> accountsToAdd = new HashSet<>(accountsToSync);
        Set<Account> accountsToRemove = new HashSet<>();

        for (Account existingAccount : customer.getAccounts()) {
            boolean shouldKeep = safeAccountIds.contains(existingAccount.getId());

            if (!shouldKeep) {
                accountsToRemove.add(existingAccount);
            } else {
                accountsToAdd.remove(existingAccount);
            }
        }

        addAccountsToCustomer(customer, accountsToAdd);
        removeAccountsFromCustomer(customer, accountsToRemove);
    }

    private void addAccountsToCustomer(Customer customer, Set<Account> accountsToAdd) {
        for (Account account : accountsToAdd) {
            customer.getAccounts().add(account);

            account.getOwners().add(customer);
        }
    }

    private void removeAccountsFromCustomer(Customer customer, Set<Account> accountsToRemove) {
        for (Account account : accountsToRemove) {
            customer.getAccounts().remove(account);

            account.getOwners().remove(customer);
            accountRepository.save(account);
        }

    }
}