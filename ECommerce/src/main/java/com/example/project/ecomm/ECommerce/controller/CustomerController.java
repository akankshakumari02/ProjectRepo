package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController
{
    private CustomerService customerService;

    @Autowired
   public CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCustomer(@RequestBody Customer customer, final HttpServletRequest request) {
        System.out.println(customer);
        customerService.register(customer);
        return new ResponseEntity<>("Account successfully created... Check your mail for activation Link..", HttpStatus.OK);
    }
}
