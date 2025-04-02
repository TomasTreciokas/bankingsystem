package com.danskebank.banking.customer.rest;

import com.danskebank.banking.customer.rest.dto.request.CustomerCreateRequest;
import com.danskebank.banking.customer.rest.dto.request.CustomerUpdateRequest;
import com.danskebank.banking.customer.rest.dto.response.CustomerResponse;
import com.danskebank.banking.customer.shared.model.Customer;
import com.danskebank.banking.customer.shared.model.mapper.CustomerResponseMapper;
import com.danskebank.banking.customer.shared.service.CustomerSearchRequest;
import com.danskebank.banking.customer.shared.service.CustomerSearchService;
import com.danskebank.banking.customer.shared.service.CustomerServiceResolver;
import com.danskebank.banking.general.dto.PageableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer data")
public class CustomerController {

    private final CustomerServiceResolver customerServiceResolver;
    private final CustomerSearchService customerSearchService;

    @Autowired
    public CustomerController(CustomerServiceResolver customerServiceResolver, CustomerSearchService customerSearchService) {
        this.customerServiceResolver = customerServiceResolver;
        this.customerSearchService = customerSearchService;
    }

    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email/phone already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest) {
        try {
            Customer createdCustomer = customerServiceResolver.resolveService(customerCreateRequest.getCustomerType()).createCustomer(customerCreateRequest);

            return new ResponseEntity<>(
                    CustomerResponseMapper.mapFrom(createdCustomer),
                    HttpStatus.CREATED
            );
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update a customer", description = "Updates a customer with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email/phone already exists", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody CustomerUpdateRequest request, @PathVariable("id") long id) {
        try {
            CustomerResponse createdCustomer = customerServiceResolver.resolveService(request.getCustomerType()).updateCustomer(id, request);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Search for customers", description = "Search for customers based on a search searchCriteria with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableResponse.class)))
    })
    @PostMapping("/search")
    public ResponseEntity<PageableResponse<CustomerResponse>> searchCustomers(
            @Parameter(description = "Term to search for in customer fields") @Valid @RequestBody CustomerSearchRequest searchRequest,
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size
    ) {
        PageableResponse<CustomerResponse> customers = customerSearchService.searchCustomers(searchRequest, page, size);

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
} 