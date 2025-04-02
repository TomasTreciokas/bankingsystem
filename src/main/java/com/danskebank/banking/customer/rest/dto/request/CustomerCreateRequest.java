package com.danskebank.banking.customer.rest.dto.request;

import com.danskebank.banking.customer.address.rest.dto.create.AddressCreateRequest;
import com.danskebank.banking.customer.shared.model.enums.CustomerType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CustomerCreateRequest {

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{8,15}$", message = "Phone number must be 8-15 digits, can include +, spaces, and hyphens")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;

    @Size(max = 50, message = "Private reference code cannot exceed 50 characters")
    private String privateReferenceCode;

    @Size(max = 50, message = "National ID cannot exceed 50 characters")
    private String nationalId;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Valid
    private List<AddressCreateRequest> addresses;

    @NotNull(message = "Account IDs list is required (can be empty)")
    private List<Long> accountIds;
} 