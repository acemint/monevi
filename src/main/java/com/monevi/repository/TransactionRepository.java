package com.monevi.repository;

import com.monevi.entity.Report;
import com.monevi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
