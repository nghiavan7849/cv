package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
