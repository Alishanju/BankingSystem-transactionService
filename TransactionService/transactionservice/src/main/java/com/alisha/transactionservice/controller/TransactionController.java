package com.alisha.transactionservice.controller;

import com.alisha.transactionservice.dto.TransactionRequest;
import com.alisha.transactionservice.dto.TransactionResponse;
import com.alisha.transactionservice.dto.TransactionUpdateRequest;
import com.alisha.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction API", description = "Transaction Management APIs")
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    @Operation(summary = "Create Transaction", description = "Creates a new transaction")
    public TransactionResponse create(
            @Valid @RequestBody TransactionRequest request) {

        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "Get All Transactions", description = "Fetch paginated transactions")
    public Page<TransactionResponse> getAll(
            @PageableDefault(size = 5) Pageable pageable) {

        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Transaction By Id", description = "Fetch transaction by id")
    public TransactionResponse getById(
            @PathVariable Long id) {

        return service.getById(id);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get Transactions By Customer Id", description = "Fetch transactions by Customer Id")

    public List<TransactionResponse> getByCustomer(
            @PathVariable Long customerId) {

        return service.getByCustomer(customerId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update Transaction", description = "Update status and description")

    public TransactionResponse update(
            @PathVariable Long id,
            @RequestBody TransactionUpdateRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Transaction by id", description = "Delete transaction by id")

    public void delete(
            @PathVariable Long id) {

        service.delete(id);
    }

    @DeleteMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Transaction by customerId", description = "Delete transaction by customerId")

    public void deleteByCustomerId(
            @PathVariable Long customerId) {

        service.deleteByCustomerId(customerId);
    }
}