package com.alisha.transactionservice.service;

import com.alisha.transactionservice.client.CustomerClient;
import com.alisha.transactionservice.exception.CustomerServiceUnavailableException;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerValidationService {

    private final CustomerClient customerClient;

    @Retry(name = "customerService")
    @CircuitBreaker(
            name = "customerService",
            fallbackMethod = "customerFallback")
    public void validateCustomer(
            Long customerId) {

        log.info(
                "Validating customer {}",
                customerId);

        customerClient.getCustomer(
                customerId);

        log.info(
                "Customer validation successful {}",
                customerId);
    }

    public void customerFallback(
            Long customerId,
            Exception ex) {

        log.error(
                "Fallback triggered for customer {}",
                customerId);

        throw new CustomerServiceUnavailableException(
                "Customer Service is currently unavailable");
    }
}