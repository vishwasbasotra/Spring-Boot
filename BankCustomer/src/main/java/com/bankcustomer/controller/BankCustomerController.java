package com.bankcustomer.controller;

import com.bankcustomer.entity.BankCustomer;
import com.bankcustomer.service.BankCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankCustomerController {

    @Autowired
    private BankCustomerService bankCustomerService;

    @GetMapping("/public/bankcustomer")
    public ResponseEntity<List<BankCustomer>> getAllBankCustomer(){
        return new ResponseEntity<>(bankCustomerService.getAllBankCustomers(), HttpStatus.OK);
    }

    @GetMapping("/public/bankcustomer/{customerId}")
    public ResponseEntity<BankCustomer> findBankCustomer(@PathVariable long customerId){
        return new ResponseEntity<>(bankCustomerService.searchCustomer(customerId), HttpStatus.FOUND);
    }

    @PostMapping("/admin/bankcustomer")
    public ResponseEntity<String> addNewCustomer(@RequestBody BankCustomer bankCustomer){
        try{
            String status = bankCustomerService.addCustomer(bankCustomer);
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }

    }

    @DeleteMapping("/admin/bankcustomer/{customerId}")
    public ResponseEntity<String> deleteBankCustomer(@PathVariable long customerId){
        try{
            String status = bankCustomerService.deleteCustomer(customerId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (ResponseStatusException e){
               return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/admin/bankcustomer/{customerId}")
    public ResponseEntity<String> updateBankCustomerEmail(@PathVariable long customerId, @RequestBody BankCustomer bankCustomer){
        try{
            String status = bankCustomerService.updateCustomer(customerId, bankCustomer);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
