package com.danskebank.banking.account.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$", message = "Account number must be 6-20 alphanumeric characters")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;
} 