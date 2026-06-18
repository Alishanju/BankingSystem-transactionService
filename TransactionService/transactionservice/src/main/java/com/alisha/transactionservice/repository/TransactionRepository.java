package com.alisha.transactionservice.repository;

import com.alisha.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

    Page<Transaction> findAll(Pageable pageable);

}