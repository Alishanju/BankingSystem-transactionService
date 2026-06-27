package com.alisha.transactionservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.alisha.transactionservice.dto.TransactionRequest;
import com.alisha.transactionservice.dto.TransactionResponse;
import com.alisha.transactionservice.entity.Transaction;
import com.alisha.transactionservice.enums.TransactionStatus;
import com.alisha.transactionservice.enums.TransactionType;
import com.alisha.transactionservice.exception.CustomerNotFoundException;
import com.alisha.transactionservice.repository.TransactionRepository;

import brave.Request;
import feign.FeignException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//testing -> arrange-act-assert
/*Arrange
│
├── Create input
├── Configure mocks
│
Act
│
└── Call service
│
Assert
│
├── Verify returned data
├── Verify interactions
└── (Optional) Capture arguments */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private CustomerValidationService customerValidationService;

    @InjectMocks
    private TransactionService service;
    private TransactionRequest request;

    @BeforeAll
    static void beforeAll() {

        System.out.println("========== Starting TransactionService Tests ==========");

    }

    @BeforeEach
    void setup() {

        request = new TransactionRequest();

        request.setCustomerId(1L);

        request.setAmount(BigDecimal.valueOf(1000));

        request.setTransactionType(TransactionType.DEPOSIT);

        request.setDescription("Salary");

    }

    @Test
    @DisplayName("Should create transaction successfully when customer exists")
    void shouldCreateTransactionSuccessfully() {

        // ==========================================================
        // Arrange
        // ==========================================================

        Transaction savedTransaction = Transaction.builder()
                .id(100L)
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .transactionType(request.getTransactionType())
                .description(request.getDescription())
                .status(TransactionStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(Transaction.class)))
                .thenReturn(savedTransaction);

        // ==========================================================
        // Act
        // ==========================================================

        TransactionResponse response = service.create(request);

        // ==========================================================
        // Assert (Verify returned response)
        // ==========================================================

        assertAll("Verify returned TransactionResponse",

                () -> assertEquals(
                        100L,
                        response.getId()),

                () -> assertEquals(
                        request.getCustomerId(),
                        response.getCustomerId()),

                () -> assertEquals(
                        request.getAmount(),
                        response.getAmount()),

                () -> assertEquals(
                        request.getTransactionType(),
                        response.getTransactionType()),

                () -> assertEquals(
                        TransactionStatus.SUCCESS,
                        response.getStatus()),

                () -> assertEquals(
                        request.getDescription(),
                        response.getDescription())

        );

        // ==========================================================
        // Verify interactions
        // ==========================================================

        verify(customerValidationService, times(1))
                .validateCustomer(request.getCustomerId());

        // ==========================================================
        // Capture the Transaction passed to repository.save()
        // ==========================================================

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(repository, times(1))
                .save(transactionCaptor.capture());

        Transaction capturedTransaction = transactionCaptor.getValue();

        // ==========================================================
        // Assert captured Transaction object
        // ==========================================================

        assertAll("Verify Transaction passed to Repository",

                () -> assertEquals(
                        request.getCustomerId(),
                        capturedTransaction.getCustomerId()),

                () -> assertEquals(
                        request.getAmount(),
                        capturedTransaction.getAmount()),

                () -> assertEquals(
                        request.getTransactionType(),
                        capturedTransaction.getTransactionType()),

                () -> assertEquals(
                        request.getDescription(),
                        capturedTransaction.getDescription()),

                () -> assertEquals(
                        TransactionStatus.SUCCESS,
                        capturedTransaction.getStatus())

        );

    }
    // @Test
    // @DisplayName("Should create transaction successfully")
    // void shouldCreateTransactionSuccessfully() {
    // // Arrange
    // Transaction savedTransaction = Transaction.builder()
    // .id(100L)
    // .customerId(request.getCustomerId())
    // .amount(request.getAmount())
    // .transactionType(request.getTransactionType())
    // .description(request.getDescription())
    // .status(TransactionStatus.SUCCESS)
    // .createdAt(LocalDateTime.now())
    // .build();
    // when(repository.save(any(Transaction.class))).thenReturn(savedTransaction);

    // // Act
    // TransactionResponse response = service.create(request);
    // // Assert
    // assertAll("verify customer creation and repository interactions",
    // () -> assertEquals(100L, response.getId()),
    // () -> assertEquals(
    // request.getCustomerId(),
    // response.getCustomerId()),
    // () -> assertEquals(
    // TransactionStatus.SUCCESS,
    // response.getStatus()));

    // verify(customerValidationService).validateCustomer(request.getCustomerId());
    // verify(repository, times(1))
    // .save(any(Transaction.class));

    // }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer does not exist")
    void shouldThrowCustomerNotFoundException() {

        // Arrange

        doThrow(FeignException.NotFound.class)
                .when(customerValidationService)
                .validateCustomer(request.getCustomerId());

        // Act + Assert

        CustomerNotFoundException thrown = assertThrows(
                CustomerNotFoundException.class,
                () -> service.create(request));

        assertEquals(
                "Customer not found with id 1",
                thrown.getMessage());

        // Verify

        verify(customerValidationService)
                .validateCustomer(request.getCustomerId());

        verify(repository, never())
                .save(any(Transaction.class));

    }

    @AfterEach
    void tearDown() {

        System.out.println("Test Finished");

    }

    @AfterAll
    static void afterAll() {

        System.out.println("========== Completed TransactionService Tests ==========");

    }

}
