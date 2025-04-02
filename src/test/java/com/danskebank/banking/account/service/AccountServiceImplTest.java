package com.danskebank.banking.account.service;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.repository.AccountRepository;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.account.validator.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountValidator accountValidator;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountRequest validAccountRequest;
    private Account savedAccount;

    @BeforeEach
    void setUp() {
        // Setup test data
        validAccountRequest = AccountRequest.builder()
                .accountNumber("TEST123456")
                .balance(new BigDecimal("1000.00"))
                .build();

        savedAccount = Account.builder()
                .id(1L)
                .accountNumber("TEST123456")
                .balance(new BigDecimal("1000.00"))
                .build();
    }

    @Test
    void createAccount_WithValidRequest_ShouldReturnCreatedAccount() {
        // Arrange
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        Account result = accountService.createAccount(validAccountRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TEST123456", result.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());

        // Verify
        verify(accountRepository).existsByAccountNumber(validAccountRequest.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getAccount_WithExistingId_ShouldReturnAccount() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(savedAccount));

        // Act
        Account result = accountService.getAccount(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TEST123456", result.getAccountNumber());

        // Verify
        verify(accountRepository).findById(1L);
    }

    @Test
    void getAccount_WithNonExistingId_ShouldThrowException() {
        // Arrange
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            accountService.getAccount(999L);
        });

        // Verify
        verify(accountRepository).findById(999L);
    }
} 