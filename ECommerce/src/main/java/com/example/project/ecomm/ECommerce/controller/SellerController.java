package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.service.SellerService;
import com.example.project.ecomm.ECommerce.validatorobjects.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class SellerController
{
    @Autowired
    private SellerService sellerService;


    // Registration

    @PostMapping("/register/seller")
    public ResponseEntity<Object> registerSeller(@Valid @RequestBody Seller seller) {
        sellerService.register(seller);
        return new ResponseEntity<>("Account successfully create", HttpStatus.OK);
    }

    // Reset Password

    @PostMapping("/register/seller/forgetPasswordSeller")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody EmailDto emailDto)
    {
        sellerService.resendToken(emailDto);
        return new ResponseEntity<>("Link to reset password sent",HttpStatus.OK);
    }

    @PutMapping("/register/seller/confirmforgetpassword")
    public ResponseEntity<Object> confirmPassword(@Valid @RequestBody PasswordDto passwordDto, @RequestParam("token") String token)
    {
        sellerService.resetPassword(passwordDto,token);
        return new ResponseEntity<>("Password Successfully Reset!!", HttpStatus.OK);
    }


    // Seller API

    @GetMapping("/seller/getProfile")
    public MappingJacksonValue viewProfile(Principal principal) //user
    {
        User user = sellerService.viewProfile(principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName","lastName","contactList", "addressList", "companyName", "gst" );
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PatchMapping("/seller/updateProfile")
   public ResponseEntity<?> updateProfile(@Valid Principal principal, @RequestBody ProfileDto profileDto)
    {
        sellerService.updateProfile(principal.getName(), profileDto);
        return new ResponseEntity<>("Profile Updated", HttpStatus.OK);
    }

    @PatchMapping("/seller/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid Principal principal, @RequestBody PasswordDto passwordDto)
    {
        sellerService.updatePassword(principal.getName(), passwordDto);
        return new ResponseEntity<>("Password Updated", HttpStatus.OK);
    }

    @PutMapping("/seller/updateAddress")
    public ResponseEntity<String> updateAddress(@Valid @RequestBody AddressDto addressDto, @RequestParam("id") int id)
    {
        sellerService.updateAddress(addressDto,id);
        return new ResponseEntity<>("Address Updated", HttpStatus.OK);

    }

    //Category API

    @GetMapping("/seller/getcategory")
    public MappingJacksonValue getCategory()
    {
        Iterable<Category> list = sellerService.getAllCategory();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name" );
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }


    //Product API

    @PostMapping("/seller/addproduct")
    public ResponseEntity<Object> addProduct(@RequestBody ProductDto productDto, Principal principal)
    {
        sellerService.addProduct(productDto,principal.getName());
        return new ResponseEntity<>("Link to Activate Product sent", HttpStatus.OK);
    }

    @GetMapping("/seller/getproduct")
    public MappingJacksonValue getProduct(@RequestParam("id") Integer id, Principal principal)
    {
        Product product = sellerService.getProduct(id, principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/seller/getallproduct")
    public MappingJacksonValue getAllProduct(Principal principal)
    {
        List<Product> list = sellerService.getAllProducts(principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @DeleteMapping("/seller/deleteproduct")
    public ResponseEntity<String> deleteProduct(@RequestParam Integer id, Principal principal)
    {
        sellerService.deleteproduct(id, principal.getName());
        return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
    }

    @PutMapping("/seller/updateproduct")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDto productDto, @RequestParam Integer id)
    {
        sellerService.updateProduct(productDto, id);
        return new ResponseEntity<>("Product Updated", HttpStatus.OK);
    }
}
