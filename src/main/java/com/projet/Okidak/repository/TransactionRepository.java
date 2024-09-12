package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
    
}
