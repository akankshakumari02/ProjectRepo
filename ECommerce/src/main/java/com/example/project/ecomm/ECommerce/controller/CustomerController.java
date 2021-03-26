package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class CustomerController
{
    @Autowired
    CustomerService customerService;

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer)
    {
        Customer customer1 = customerService.addCustomer(customer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(customer1.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/getCustomer/{id}")
    public Customer getCustomer(@PathVariable int id) throws Exception {

        return customerService.getCustomer(id);

    }

    @GetMapping("/getAllCustomers")
    public List<Customer> getAllCustomers() throws Exception {

        return customerService.getAllCustomers();
    }
}
