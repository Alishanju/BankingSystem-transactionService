package com.alisha.transactionservice.repository;

import com.alisha.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

    Page<Transaction> findAll(Pageable pageable);

    @Modifying
    @Query("""
            delete from Transaction t
            where t.customerId = :customerId
            """)
    int deleteTransactionsByCustomerId(
            @Param("customerId") Long customerId);

}