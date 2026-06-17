package com.alisha.transactionservice.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/transactions/test")
    public String test() {

        return "Transaction Service Secured";
    }
}