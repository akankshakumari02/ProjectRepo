package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.AdminService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdminController
{
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/admin/getCustomer")
    public MappingJacksonValue getCustomerDetails() {
        Iterable<Customer> list = adminService.getCustomer();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName","middleName","lastName","email","Is_Active");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    /*public Iterable<Customer> getCustomerDetails() {


        return adminService.getCustomer();
    }*/



    @GetMapping("/admin/getSeller")
    public MappingJacksonValue getSellerDetails() {
        Iterable<Seller> list = adminService.getSeller();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName","middleName","lastName","email","Is_Active","companyName","addressList","contactList");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

   /* public Iterable<Seller> getSellerDetails() {
        return adminService.getSeller();
    }
*/
    @PutMapping("/admin/activateCustomer/{id}")
    public ResponseEntity<String> activateCustomer(@Valid @PathVariable int id){
        return adminService.activateCustomer(id);
    }

    @PutMapping("/admin/deactivateCustomer/{id}")
    public ResponseEntity<String> deActivateCustomer(@Valid @PathVariable int id){
        return adminService.deActivateCustomer(id);
    }

    @PutMapping("/admin/activateSeller/{id}")
    public ResponseEntity<String> activateSeller(@Valid @PathVariable int id){
        return adminService.activateSeller(id);
    }

    @PutMapping("/admin/deactivateSeller/{id}")
    public ResponseEntity<String> deActivateSeller(@Valid @PathVariable int id){
        return adminService.deActivateSeller(id);
    }
}
