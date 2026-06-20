package com.alisha.transactionservice.dto;

import lombok.Data;

@Data
public class CustomerResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String username;

    private String role;
}