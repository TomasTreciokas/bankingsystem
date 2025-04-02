package com.danskebank.banking.integration;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.repository.AccountRepository;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        // Clean up database before each test
        accountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up database after each test
        accountRepository.deleteAll();
    }

    @Test
    void createAccount_ThenGetByIdShouldReturnSameAccount() throws Exception {
        // Arrange
        AccountRequest newAccount = new AccountRequest();
        newAccount.setAccountNumber("INTTEST123");
        newAccount.setBalance(new BigDecimal("2500.00"));

        // Act
        MvcResult createResult = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountNumber").value("INTTEST123"))
                .andExpect(jsonPath("$.balance").value(2500.00))
                .andReturn();

        String createResponseJson = createResult.getResponse().getContentAsString();
        Integer accountId = objectMapper.readTree(createResponseJson).get("id").asInt();

        // Act
        mockMvc.perform(get("/api/accounts/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.accountNumber").value("INTTEST123"))
                .andExpect(jsonPath("$.balance").value(2500.00));

        Account savedAccount = accountRepository.findById((long) accountId).orElse(null);
        assertNotNull(savedAccount);
        assertEquals("INTTEST123", savedAccount.getAccountNumber());
        assertEquals(0, new BigDecimal("2500.00").compareTo(savedAccount.getBalance()));
    }

    @Test
    void createDuplicateAccount_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AccountRequest newAccount = new AccountRequest();
        newAccount.setAccountNumber("DUPLICATE123");
        newAccount.setBalance(new BigDecimal("1000.00"));

        // Act
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated());

        // Act
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNonexistentAccount_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts/9999"))
                .andExpect(status().isNotFound());
    }
} 