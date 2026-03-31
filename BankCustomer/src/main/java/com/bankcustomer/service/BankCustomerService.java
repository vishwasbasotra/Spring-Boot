package com.bankcustomer.service;

import com.bankcustomer.entity.BankCustomer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankCustomerService {
    BankCustomer searchCustomer(long customerId);
    List<BankCustomer> getAllBankCustomers();
    String addCustomer(BankCustomer bankCustomer);
    String deleteCustomer(long customerId);
    String updateCustomer(long customerId, BankCustomer bankCustomer);
}
