package com.alisha.transactionservice.service;

import com.alisha.transactionservice.dto.TransactionRequest;
import com.alisha.transactionservice.dto.TransactionResponse;
import com.alisha.transactionservice.dto.TransactionUpdateRequest;
import com.alisha.transactionservice.entity.Transaction;
import com.alisha.transactionservice.enums.TransactionStatus;
import com.alisha.transactionservice.exception.CustomerNotFoundException;
import com.alisha.transactionservice.exception.TransactionNotFoundException;
import com.alisha.transactionservice.repository.TransactionRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

        private final TransactionRepository repository;
        private final CustomerValidationService customerValidationService;

        private TransactionResponse map(Transaction txn) {

                return TransactionResponse.builder()
                                .id(txn.getId())
                                .customerId(txn.getCustomerId())
                                .amount(txn.getAmount())
                                .transactionType(txn.getTransactionType())
                                .status(txn.getStatus())
                                .description(txn.getDescription())
                                .createdAt(txn.getCreatedAt())
                                .build();
        }

        @Transactional
        public TransactionResponse create(
                        TransactionRequest request) {

                Transaction transaction = Transaction.builder()
                                .customerId(request.getCustomerId())
                                .amount(request.getAmount())
                                .transactionType(request.getTransactionType())
                                .description(request.getDescription())
                                .status(TransactionStatus.SUCCESS)
                                .createdAt(LocalDateTime.now())
                                .build();
                try {
                        log.info(
                                        "Creating transaction for customer id {}",
                                        request.getCustomerId());

                        customerValidationService
                                        .validateCustomer(
                                                        request.getCustomerId());

                } catch (FeignException.NotFound ex) {

                        throw new CustomerNotFoundException(
                                        "Customer not found with id "
                                                        + request.getCustomerId());
                }

                Transaction saved = repository.save(transaction);

                log.info("Transaction created with id {}", saved.getId());

                // if (true) {
                // throw new RuntimeException(
                // "Testing rollback");
                // }

                return map(saved);
        }

        public Page<TransactionResponse> getAll(
                        Pageable pageable) {
                log.info("Fetching all customers via pagination");
                return repository.findAll(pageable)
                                .map(this::map);
        }

        public TransactionResponse getById(Long id) {
                log.info("Fetching customer by id");
                Transaction txn = repository.findById(id)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                return map(txn);
        }

        public List<TransactionResponse> getByCustomer(
                        Long customerId) {
                log.info("Fetching all customers by customerId");
                return repository.findByCustomerId(customerId)
                                .stream()
                                .map(this::map)
                                .toList();
        }

        @Transactional
        public TransactionResponse update(
                        Long id,
                        TransactionUpdateRequest request) {
                log.info("Updating customer with id {}", id);
                Transaction transaction = repository.findById(id)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                transaction.setDescription(
                                request.getDescription());

                transaction.setStatus(
                                request.getStatus());

                transaction.setUpdatedAt(
                                LocalDateTime.now());

                Transaction saved = repository.save(transaction);

                return map(saved);
        }

        @Transactional
        public void delete(Long id) {
                log.info("Deleting customer with id {}", id);
                Transaction txn = repository.findById(id)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                repository.delete(txn);
                log.info("Customer deleted successfully with id {}", id);
        }

        @Transactional
        public void deleteByCustomerId(Long customerId) {
                log.info("Deleting customer with customer id {}", customerId);
                int deleted = repository.deleteTransactionsByCustomerId(customerId);
                log.info("Customers deleted successfully with customer id {}", customerId);
                log.info("{} transactions deleted", deleted);

        }
}