package com.alisha.transactionservice.config;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.retry.RetryRegistry;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RetryEventConfig {

    private final RetryRegistry retryRegistry;

    @PostConstruct
    public void registerRetryEvents() {

        retryRegistry
                .retry("customerService")
                .getEventPublisher()

                .onRetry(event ->
                        log.warn(
                                "Retry attempt {} for customerService",
                                event.getNumberOfRetryAttempts()))

                .onSuccess(event ->
                        log.info(
                                "Retry successful for customerService"))

                .onError(event ->
                        log.error(
                                "Retry failed for customerService"));
    }
}