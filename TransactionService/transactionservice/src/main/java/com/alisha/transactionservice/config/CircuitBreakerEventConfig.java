package com.alisha.transactionservice.config;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerEventConfig {

    private final CircuitBreakerRegistry registry;

    @PostConstruct
    public void registerEvents() {

        registry
                .circuitBreaker("customerService")
                .getEventPublisher()

                .onStateTransition(event ->
                        log.warn(
                                "Circuit Breaker State Change {}",
                                event.getStateTransition()))

                .onCallNotPermitted(event ->
                        log.error(
                                "Call blocked because circuit is OPEN"));
    }
}