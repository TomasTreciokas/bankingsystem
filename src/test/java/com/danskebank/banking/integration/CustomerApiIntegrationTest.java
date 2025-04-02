package com.danskebank.banking.integration;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.repository.AccountRepository;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.repository.CustomerRepository;
import com.danskebank.banking.customer.shared.service.CustomerSearchRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CustomerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Long accountId;

    @BeforeEach
    void setUp() throws Exception {
        // Create
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber("CUSTTEST123");
        accountRequest.setBalance(new BigDecimal("5000.00"));

        MvcResult result = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        accountId = objectMapper.readTree(responseJson).get("id").asLong();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createIndividualCustomer_ShouldReturnCreatedCustomer() throws Exception {
        // Arrange
        CustomerCreateRequest customerRequest = CustomerCreateRequest.builder()
                .customerType(CustomerType.INDIVIDUAL)
                .name("Jane")
                .lastName("Smith")
                .phoneNumber("9876543210")
                .email("jane.smith@example.com")
                .nationalId("ID67890")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .accountIds(List.of(accountId))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.customerType").value("INDIVIDUAL"));

        // Verify
        Account account = accountRepository.findById(accountId).orElse(null);
        assertNotNull(account);
        assertEquals(1, account.getOwners().size());
    }

    @Test
    void createPrivateCustomer_ShouldReturnCreatedCustomer() throws Exception {
        // Arrange
        CustomerCreateRequest customerRequest = CustomerCreateRequest.builder()
                .customerType(CustomerType.PRIVATE)
                .name("ABC Company")
                .phoneNumber("1122334455")
                .email("contact@abccompany.com")
                .privateReferenceCode("PRIV123")
                .accountIds(List.of(accountId))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("ABC Company"))
                .andExpect(jsonPath("$.email").value("contact@abccompany.com"))
                .andExpect(jsonPath("$.customerType").value("PRIVATE"))
                .andExpect(jsonPath("$.privateReferenceCode").value("PRIV123"));
    }

    @Test
    void searchCustomers_ShouldReturnMatchingCustomers() throws Exception {
        // Arrange
        CustomerCreateRequest individual = CustomerCreateRequest.builder()
                .customerType(CustomerType.INDIVIDUAL)
                .name("Test")
                .lastName("Individual")
                .phoneNumber("1231231231")
                .email("test.individual@example.com")
                .accountIds(Collections.emptyList())
                .build();

        CustomerCreateRequest privateCustomer = CustomerCreateRequest.builder()
                .customerType(CustomerType.PRIVATE)
                .name("Test Private")
                .phoneNumber("3213213213")
                .email("test.private@example.com")
                .privateReferenceCode("TEST001")
                .accountIds(Collections.emptyList())
                .build();

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(individual)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(privateCustomer)))
                .andExpect(status().isCreated());

        // Act & Assert
        CustomerSearchRequest searchRequest = new CustomerSearchRequest(
                null,        // searchCriteria
                CustomerType.INDIVIDUAL,  // customerType
                null,        // nationalId
                null,        // dateOfBirth
                null,        // registrationNumber
                null         // privateReferenceCode
        );

        mockMvc.perform(post("/api/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test"))
                .andExpect(jsonPath("$.content[0].lastName").value("Individual"))
                .andExpect(jsonPath("$.content[0].customerType").value("INDIVIDUAL"));

        CustomerSearchRequest nameSearch = new CustomerSearchRequest(
                "Private",   // searchCriteria - use this for name search
                null,        // customerType
                null,        // nationalId
                null,        // dateOfBirth
                null,        // registrationNumber
                null         // privateReferenceCode
        );

        mockMvc.perform(post("/api/customers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nameSearch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Test Private"))
                .andExpect(jsonPath("$.content[0].customerType").value("PRIVATE"));
    }
} 