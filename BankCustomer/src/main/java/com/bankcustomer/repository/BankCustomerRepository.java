package com.bankcustomer.repository;

import com.bankcustomer.entity.BankCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCustomerRepository extends JpaRepository<BankCustomer, Long> {
}
