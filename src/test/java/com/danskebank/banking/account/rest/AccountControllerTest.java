package com.danskebank.banking.account.rest;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private AccountRequest validAccountRequest;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        validAccountRequest = new AccountRequest();
        validAccountRequest.setAccountNumber("TEST123456");
        validAccountRequest.setBalance(new BigDecimal("1000.00"));

        mockAccount = Account.builder()
                .id(1L)
                .accountNumber("TEST123456")
                .balance(new BigDecimal("1000.00"))
                .owners(new HashSet<>())
                .build();
    }

    @Test
    void createAccount_WithValidRequest_ShouldReturnCreatedAccount() throws Exception {
        // Arrange
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(mockAccount);

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("TEST123456"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.numberOfOwners").value(0));
    }

    @Test
    void createAccount_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AccountRequest invalidRequest = new AccountRequest();
        invalidRequest.setBalance(new BigDecimal("1000.00"));

        // Mock
        when(accountService.createAccount(any(AccountRequest.class)))
                .thenThrow(new IllegalArgumentException("Account number cannot be null"));

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Account number cannot be null"));
    }

    @Test
    void getAccount_WithExistingId_ShouldReturnAccount() throws Exception {
        // Arrange
        when(accountService.getAccount(eq(1L))).thenReturn(mockAccount);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("TEST123456"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void getAccount_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(accountService.getAccount(eq(999L))).thenThrow(new EntityNotFoundException("Account not found"));

        // Act & Assert
        mockMvc.perform(get("/api/accounts/999"))
                .andExpect(status().isNotFound());
    }
} 