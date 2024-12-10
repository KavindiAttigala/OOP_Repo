package com.ticketing.repository;

import com.ticketing.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.customerId = :customerId WHERE t.customerId IS NULL AND t.vendorId IS NOT NULL")
    int updateCustomerIdForVendorTransactions(String customerId);


}
