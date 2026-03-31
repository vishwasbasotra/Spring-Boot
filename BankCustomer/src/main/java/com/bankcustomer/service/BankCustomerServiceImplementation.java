package com.bankcustomer.service;

import com.bankcustomer.entity.BankCustomer;
import com.bankcustomer.repository.BankCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class BankCustomerServiceImplementation implements BankCustomerService{

    @Autowired
    private BankCustomerRepository bankCustomerRepository;

    @Override
    public List<BankCustomer> getAllBankCustomers() {
        return bankCustomerRepository.findAll();
    }

    @Override
    public BankCustomer searchCustomer(long customerId) {
        return bankCustomerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Customer Not Found!!!"));
    }

    @Override
    public String addCustomer(BankCustomer bankCustomer) {
        bankCustomerRepository.save(bankCustomer);
        return "Bank Customer Saved Successfully";
    }

    @Override
    public String deleteCustomer(long customerId) {
        BankCustomer bankCustomer = bankCustomerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Customer Not Found!!!"));
        bankCustomerRepository.delete(bankCustomer);
        return "Bank Customer: "+customerId+" Delected Successfully";
    }

    @Override
    public String updateCustomer(long customerId, BankCustomer bankCustomer) {
        BankCustomer existingbankCustomer = bankCustomerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Customer Not Found!!!"));
        existingbankCustomer.setEmail(bankCustomer.getEmail());
        bankCustomerRepository.save(existingbankCustomer);
        return "Bank Customer: "+customerId+" Updated Successfully";
    }
}
