package com.contactmanagementapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactManagementController {

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String getContacts(){
        return "Returning all contacts";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String addNewContact(){
        return "Added new contact";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable long id){
        return "Deleted contact id: "+id;
    }

    @GetMapping("/public/info")
    public String publicInfo(){
        return "This is a public endpoint";
    }
}
