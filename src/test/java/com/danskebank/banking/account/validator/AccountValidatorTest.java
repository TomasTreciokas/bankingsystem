package com.danskebank.banking.account.validator;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.repository.AccountRepository;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountValidatorTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountValidatorImpl accountValidator;

    private Account account1;
    private Account account2;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Set up test accounts
        account1 = Account.builder().id(1L).accountNumber("ACC001").build();
        account2 = Account.builder().id(2L).accountNumber("ACC002").build();

        // Set up test customer
        customer = CustomerIndividual.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    void validateAccountOwnerDuplicate_WithNonDuplicateCustomer_ShouldNotThrowException() {
        // Arrange
        Set<Customer> existingOwners = new HashSet<>();

        // Act & Assert
        assertDoesNotThrow(() -> accountValidator.validateAccountOwnerDuplicate(existingOwners, customer));
    }

    @Test
    void validateAccountOwnerDuplicate_WithDuplicateCustomer_ShouldThrowException() {
        // Arrange
        Set<Customer> existingOwners = new HashSet<>();
        existingOwners.add(customer);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            accountValidator.validateAccountOwnerDuplicate(existingOwners, customer);
        });

        // Verify message
        assertTrue(exception.getMessage().contains("already linked"));
    }

    @Test
    void validateIfAllAccountsMatch_WithValidAccounts_ShouldNotThrowException() {
        // Arrange
        List<Long> accountIds = List.of(1L, 2L);
        List<Account> accounts = Arrays.asList(account1, account2);

        // Act & Assert
        assertDoesNotThrow(() -> accountValidator.validateIfAllAccountsMatch(accountIds, accounts));
    }

    @Test
    void validateIfAllAccountsMatch_WithMismatchedAccounts_ShouldThrowException() {
        // Arrange
        List<Long> accountIds = List.of(1L, 3L);
        List<Account> accounts = List.of(account1);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountValidator.validateIfAllAccountsMatch(accountIds, accounts);
        });

        // Verify
        assertTrue(exception.getMessage().contains("not found"));
    }
} 