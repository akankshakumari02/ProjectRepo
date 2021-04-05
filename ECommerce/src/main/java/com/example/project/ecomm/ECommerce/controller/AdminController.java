package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
public class AdminController
{
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/getCustomer")
    public Iterable<Customer> getCustomerDetails() {
        return adminService.getCustomer();
    }

    @GetMapping("/getSeller")
    public Iterable<Seller> getSellerDetails() {
        return adminService.getSeller();
    }

    @PutMapping("/activateCustomer/{id}")
    public ResponseEntity<String> activateCustomer(@Valid @PathVariable int id){
        return adminService.activateCustomer(id);
    }

    @PutMapping("/deactivateCustomer/{id}")
    public ResponseEntity<String> deActivateCustomer(@Valid @PathVariable int id){
        return adminService.deActivateCustomer(id);
    }

    @PutMapping("/activateSeller/{id}")
    public ResponseEntity<String> activateSeller(@Valid @PathVariable int id){
        return adminService.activateSeller(id);
    }

    @PutMapping("/deactivateSeller/{id}")
    public ResponseEntity<String> deActivateSeller(@Valid @PathVariable int id){
        return adminService.deActivateSeller(id);
    }
}
