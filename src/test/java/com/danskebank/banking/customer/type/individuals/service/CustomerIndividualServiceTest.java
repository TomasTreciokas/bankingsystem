package com.danskebank.banking.customer.type.individuals.service;

import com.danskebank.banking.account.service.AccountService;
import com.danskebank.banking.customer.address.service.AddressService;
import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import com.danskebank.banking.customer.shared.service.CustomerPersistenceService;
import com.danskebank.banking.customer.shared.validator.CustomerValidator;
import com.danskebank.banking.customer.type.individuals.model.CustomerIndividual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerIndividualServiceTest {

    @Mock
    private CustomerPersistenceService customerPersistenceService;

    @Mock
    private CustomerValidator customerValidator;

    @Mock
    private AddressService addressService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerIndividualService customerIndividualService;

    private CustomerCreateRequest validCustomerRequest;
    private CustomerIndividual savedCustomer;

    @BeforeEach
    void setUp() {
        // Setup test data
        validCustomerRequest = CustomerCreateRequest.builder()
                .customerType(CustomerType.INDIVIDUAL)
                .name("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .email("john.doe@example.com")
                .nationalId("ID12345")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .accountIds(List.of(1L))
                .build();

        savedCustomer = CustomerIndividual.builder()
                .id(1L)
                .customerType(CustomerType.INDIVIDUAL)
                .name("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .email("john.doe@example.com")
                .nationalId("ID12345")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void getSupportedType_ShouldReturnIndividualType() {
        // Act & Assert
        assertEquals(CustomerType.INDIVIDUAL, customerIndividualService.getSupportedType());
    }

    @Test
    void createCustomer_WithValidRequest_ShouldReturnCreatedCustomer() {
        // Arrange
        doNothing().when(customerValidator).validateCustomerUniqueness(any(CustomerCreateRequest.class));
        when(customerPersistenceService.save(any(Customer.class))).thenReturn(savedCustomer);
        doNothing().when(accountService).linkOwnerToAccounts(any(Customer.class), anyList());

        // Act
        Customer result = customerIndividualService.createCustomer(validCustomerRequest);

        // Assert
        assertNotNull(result);
        assertInstanceOf(CustomerIndividual.class, result);
        CustomerIndividual individualResult = (CustomerIndividual) result;
        assertEquals(1L, individualResult.getId());
        assertEquals("John", individualResult.getName());
        assertEquals("Doe", individualResult.getLastName());
        assertEquals("john.doe@example.com", individualResult.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), individualResult.getDateOfBirth());
        assertEquals(CustomerType.INDIVIDUAL, individualResult.getCustomerType());

        // Verify
        verify(customerValidator).validateCustomerUniqueness(validCustomerRequest);
        verify(customerPersistenceService).save(any(CustomerIndividual.class));
        verify(accountService).linkOwnerToAccounts(any(Customer.class), eq(List.of(1L)));
    }

    @Test
    void createCustomer_WithEmptyAccountList_ShouldStillCallLinkOwnerToAccounts() {
        // Arrange
        CustomerCreateRequest requestWithEmptyAccountsList = CustomerCreateRequest.builder()
                .customerType(CustomerType.INDIVIDUAL)
                .name("Jane")
                .lastName("Doe")
                .phoneNumber("9876543210")
                .email("jane.doe@example.com")
                .nationalId("ID67890")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .accountIds(Collections.emptyList())
                .build();

        doNothing().when(customerValidator).validateCustomerUniqueness(any(CustomerCreateRequest.class));
        when(customerPersistenceService.save(any(Customer.class))).thenReturn(savedCustomer);
        doNothing().when(accountService).linkOwnerToAccounts(any(Customer.class), anyList());

        // Act
        Customer result = customerIndividualService.createCustomer(requestWithEmptyAccountsList);

        // Assert
        assertNotNull(result);

        // Verify
        verify(customerValidator).validateCustomerUniqueness(requestWithEmptyAccountsList);
        verify(customerPersistenceService).save(any(CustomerIndividual.class));
        verify(accountService).linkOwnerToAccounts(any(Customer.class), eq(Collections.emptyList()));
    }
} 