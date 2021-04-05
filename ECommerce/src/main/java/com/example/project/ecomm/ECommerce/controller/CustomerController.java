
package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import com.example.project.ecomm.ECommerce.validatorobjects.AddressDto;
import com.example.project.ecomm.ECommerce.validatorobjects.EmailDto;
import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import com.example.project.ecomm.ECommerce.validatorobjects.ProfileDto;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
public class CustomerController
{
    private CustomerService customerService;

    @Autowired
   public CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    // Registration

    @PostMapping("/customer/register")
    public ResponseEntity<Object> registerCustomer(@Valid @RequestBody Customer customer) {
        customerService.register(customer);
        return new ResponseEntity<>("Account successfully created... Check your mail for activation Link..", HttpStatus.OK);
    }

    @PutMapping("/customer/confirm")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token)
    {
        customerService.confirmRegistration(token);
        return new ResponseEntity<>("Account Verified", HttpStatus.OK);
    }

    @PostMapping("/customer/resendToken")
    public ResponseEntity<Object> resendToken(@Valid @RequestBody EmailDto emailDto)
    {
        customerService.resendToken(emailDto);
        return new ResponseEntity<>("Activation Link Sent",HttpStatus.OK);
    }

    // Forgot Password

    @PostMapping("/forgetPasswordCustomer")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody EmailDto emailDto)
    {
        customerService.resendToken(emailDto);
        return new ResponseEntity<>("Link Sent for Resetting Password", HttpStatus.OK);
    }

    @PutMapping("/confirmPasswordCustomer")
    public ResponseEntity<Object> confirmPassword(@Valid @RequestBody PasswordDto passwordDto, @RequestParam("token") String token)
    {
            customerService.resetPassword(passwordDto, token);
            return new ResponseEntity<>("Password successfully reset", HttpStatus.OK);
    }

    // Customer API

    @GetMapping("/customer/getProfile")
   public MappingJacksonValue viewProfile(Principal principal) //user
    {
        User user = customerService.viewProfile(principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName","lastName","contactList", "Is_Active");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

   @PutMapping("/customer/updateProfile")
   public ResponseEntity<?> updateDetails(@Valid Principal principal, @RequestBody ProfileDto profileDto)
  {
      customerService.updateProfile(principal.getName(), profileDto);
      return new ResponseEntity<>("Profile Updated", HttpStatus.OK);
  }

  @PatchMapping("/customer/updateProfile/{id}")
  @Transactional
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody Customer customer)
  {
      customerService.updateProfile(customer);
      return new ResponseEntity<>("Profile Updated", HttpStatus.OK);
  }

  @PutMapping("/customer/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid Principal principal, @RequestBody PasswordDto passwordDto)
  {
      customerService.updatePassword(principal.getName(), passwordDto);
      return new ResponseEntity<>("Password Updated", HttpStatus.OK);
  }

  @GetMapping("/customer/getAddress")
    public MappingJacksonValue getAddress(Principal principal)
  {
      List<Address> addressList = customerService.addressList(principal.getName());
      SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("city","state","country","label","addressLine","zipCode");
      FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
      MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(addressList);
      mappingJacksonValue.setFilters(filterProvider);
      return mappingJacksonValue;
  }

  @PostMapping("/customer/addAddress")
    public ResponseEntity<String> addAddress(@Valid Principal principal, @RequestBody AddressDto addressDto)
  {
      customerService.addAddress(principal.getName(), addressDto);
      return new ResponseEntity<>("Address Added", HttpStatus.OK);
  }

  @DeleteMapping("/customer/deleteAddress")
    public ResponseEntity<String> deleteAddress(@RequestParam int id)
  {
      customerService.deleteAddress(id);
      return new ResponseEntity<>("Address Deleted", HttpStatus.OK);
  }

  @PutMapping("/customer/updateAddress")
    public ResponseEntity<String> updateAddress(@Valid @RequestBody AddressDto addressDto, @RequestParam int id)
  {
      customerService.updateAddress(addressDto,id);
      return new ResponseEntity<>("Address Updated", HttpStatus.OK);

  }

}


