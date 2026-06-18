package com.alisha.transactionservice.dto;

import com.alisha.transactionservice.enums.TransactionStatus;
import lombok.Data;

@Data
public class TransactionUpdateRequest {

    private String description;

    private TransactionStatus status;
}