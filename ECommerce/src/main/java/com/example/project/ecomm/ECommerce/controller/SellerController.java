package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import com.example.project.ecomm.ECommerce.service.SellerService;
import com.example.project.ecomm.ECommerce.validatorobjects.AddressDto;
import com.example.project.ecomm.ECommerce.validatorobjects.EmailDto;
import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import com.example.project.ecomm.ECommerce.validatorobjects.ProfileDto;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
public class SellerController
{
    private SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService)
    {
        this.sellerService = sellerService;
    }

    // Registration

    @PostMapping("/seller/register")
    public ResponseEntity<Object> registerSeller(@Valid @RequestBody Seller seller, final HttpServletRequest request) {
        sellerService.register(seller);
        return new ResponseEntity<>("Account successfully create", HttpStatus.OK);
    }

    // Reset Password

    @PostMapping("/forgetPasswordSeller")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody EmailDto emailDto)
    {
        sellerService.resendToken(emailDto);
        return new ResponseEntity<>("Link to reset password sent",HttpStatus.OK);
    }

    @PutMapping("/confirmPasswordSeller")
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
    public ResponseEntity<String> updateAddress(@Valid @RequestBody AddressDto addressDto, @RequestParam int id)
    {
        sellerService.updateAddress(addressDto,id);
        return new ResponseEntity<>("Address Updated", HttpStatus.OK);

    }

}
