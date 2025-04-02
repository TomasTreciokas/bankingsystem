package com.danskebank.banking.account.rest;

import com.danskebank.banking.account.model.Account;
import com.danskebank.banking.account.rest.dto.request.AccountRequest;
import com.danskebank.banking.account.rest.dto.response.AccountResponse;
import com.danskebank.banking.account.rest.dto.response.mapper.AccountResponseMapper;
import com.danskebank.banking.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account", description = "Creates a new bank account with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        Account createdAccount = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(
                AccountResponseMapper.mapFrom(createdAccount),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get account by ID", description = "Retrieves bank account details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @Parameter(description = "ID of the account to retrieve") @PathVariable Long id) {
        try {
            Account account = accountService.getAccount(id);
            return new ResponseEntity<>(
                    AccountResponseMapper.mapFrom(account),
                    HttpStatus.OK
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 