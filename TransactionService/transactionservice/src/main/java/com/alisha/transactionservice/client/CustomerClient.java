package com.alisha.transactionservice.client;

import com.alisha.transactionservice.config.FeignConfig;
import com.alisha.transactionservice.dto.CustomerResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${customer.service.url}", configuration = FeignConfig.class)
public interface CustomerClient {

    @GetMapping("/api/customers/{id}")
    CustomerResponse getCustomer(
            @PathVariable Long id);
}