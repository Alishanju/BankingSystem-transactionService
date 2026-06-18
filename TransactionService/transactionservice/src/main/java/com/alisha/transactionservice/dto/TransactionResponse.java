package com.alisha.transactionservice.dto;

import com.alisha.transactionservice.enums.TransactionStatus;
import com.alisha.transactionservice.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;

    private Long customerId;

    private BigDecimal amount;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;

    private LocalDateTime createdAt;
}