package com.danskebank.banking.customer.address.rest.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressUpdateRequest {
    public Long id;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street cannot exceed 255 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Pattern(regexp = "^[A-Za-z0-9-]{4,10}$", message = "Zip code must be 4-10 alphanumeric characters or hyphens")
    private String zipCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
}
