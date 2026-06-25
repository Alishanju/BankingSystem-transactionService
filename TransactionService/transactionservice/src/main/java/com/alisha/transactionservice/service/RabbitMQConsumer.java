package com.alisha.transactionservice.service;

import com.alisha.transactionservice.config.RabbitMQConfig;
import com.alisha.transactionservice.entity.Transaction;
import com.alisha.transactionservice.enums.TransactionStatus;
import com.alisha.transactionservice.enums.TransactionType;
import com.alisha.transactionservice.event.CustomerCreatedEvent;
import com.alisha.transactionservice.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

        private final TransactionRepository repository;

        @RabbitListener(queues = RabbitMQConfig.CUSTOMER_CREATED_QUEUE)
        public void consume(
                        CustomerCreatedEvent event, Message message) {

                String traceId = (String) message
                                .getMessageProperties()
                                .getHeaders()
                                .get("traceId");
                
    log.info(
            "Received event traceId={}",
            traceId);

                Transaction transaction = Transaction.builder()
                                .customerId(
                                                event.getCustomerId())
                                .amount(BigDecimal.ZERO)
                                .transactionType(
                                                TransactionType.DEPOSIT)
                                .status(
                                                TransactionStatus.SUCCESS)
                                .description(
                                                "Customer account initialized")
                                .createdAt(
                                                LocalDateTime.now())
                                .build();

                repository.save(transaction);

                log.info(
                                "Default transaction created for customer {}",
                                event.getCustomerId());
        }
}