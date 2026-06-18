package com.alisha.transactionservice.dto;

import com.alisha.transactionservice.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(message = "Customer Id is required")
    private Long customerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01",
            message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Transaction Type is required")
    private TransactionType transactionType;

    @Size(max = 255,
            message = "Description cannot exceed 255 chars")
    private String description;
}