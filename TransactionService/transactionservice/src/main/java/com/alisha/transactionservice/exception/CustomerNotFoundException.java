package com.alisha.transactionservice.exception;

public class CustomerNotFoundException
        extends RuntimeException {

    public CustomerNotFoundException(
            String message) {

        super(message);
    }
}