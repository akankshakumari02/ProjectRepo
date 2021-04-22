
package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import com.example.project.ecomm.ECommerce.entities.Order.Order;
import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import com.example.project.ecomm.ECommerce.validatorobjects.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    // Registration API

    @PostMapping("/register/customer")
    public ResponseEntity<Object> registerCustomer(@Valid @RequestBody CustomerDto customer) {
        customerService.register(customer);
        return new ResponseEntity<>("Account successfully created... Check your mail for activation Link..", HttpStatus.OK);
    }

    @PutMapping("/register/customer/confirmaccount")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token)
    {
        customerService.confirmRegistration(token);
        return new ResponseEntity<>("Account Verified", HttpStatus.OK);
    }

    @PostMapping("/register/customer/resendToken")
    public ResponseEntity<Object> resendToken(@Valid @RequestBody EmailDto emailDto)
    {
        customerService.resendToken(emailDto);
        return new ResponseEntity<>("Activation Link Sent",HttpStatus.OK);
    }

    // Forgot Password API

    @PostMapping("/register/customer/forgetPassword")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody EmailDto emailDto) {
        customerService.resendToken(emailDto);
        return new ResponseEntity<>("Link to reset the password has been sent.", HttpStatus.OK);
    }

    @PutMapping("/register/customer/confirmForgetPassword")
    public ResponseEntity<Object> confirmResetPassword(@Valid @RequestBody PasswordDto resetPasswordDto, @RequestParam("token") String token) {
        customerService.resetPassword(resetPasswordDto, token);
        return new ResponseEntity<>("Password reset successfully!", HttpStatus.OK);
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
    public ResponseEntity<String> deleteAddress(@RequestParam("id") int id) //provide addressId
  {
      customerService.deleteAddress(id);
      return new ResponseEntity<>("Address Deleted", HttpStatus.OK);
  }

  @PutMapping("/customer/updateAddress")
    public ResponseEntity<String> updateAddress(@Valid @RequestBody AddressDto addressDto, @RequestParam("id") int id) //provide addressId
  {
      customerService.updateAddress(addressDto,id);
      return new ResponseEntity<>("Address Updated", HttpStatus.OK);
  }


    // Category API

    @GetMapping("/customer/getCategories")
    public MappingJacksonValue getCategories() {
        Iterable<Category> list = customerService.getAllCategory();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }



    // Product API

    @GetMapping("/customer/getProduct")
    public MappingJacksonValue viewProduct(@RequestParam("id") Integer id) {
        Product product = customerService.viewProductDetails(id);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }


    @GetMapping("/customer/getProducts")
    public MappingJacksonValue viewAllProduct() {
        Iterable<Product> list = customerService.viewAllProductDetails();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @GetMapping("/customer/getSimilarProducts")
    public MappingJacksonValue viewFilteredProduct(@RequestParam("id") Integer id) {
        List<Product>  list = customerService.viewProductDetailsFiltered(id);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }


    // Cart API

    @PostMapping("/customer/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam("id") int productVariationId, @RequestParam("quantity") int quantity, @Valid Principal principal) {
        customerService.addToCart(productVariationId, quantity, principal.getName());
        return new ResponseEntity<>("Product added to the Cart ", HttpStatus.OK);
    }

    @GetMapping("/customer/viewCart")
    public MappingJacksonValue getCartProducts(@Valid Principal principal)
    {
        List<Cart> list = customerService.viewCart(principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("customer", "id", "productVariation", "quantity");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @Transactional
    @DeleteMapping("/customer/deleteFromCart")
    public ResponseEntity<String> removeFromCart(@RequestParam("id") int productVariationId, @Valid Principal principal)
    {
        customerService.deleteFromCart(productVariationId, principal.getName());
        return new ResponseEntity<>("Product deleted from the Cart ", HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/customer/updateCart")
    public ResponseEntity<String> updateCart(@RequestParam("id") int productVariationId, @RequestParam int quantity, @Valid Principal principal) {
        customerService.updateCart(productVariationId, quantity, principal.getName());
        return new ResponseEntity<>("Cart Updated!", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/customer/emptyCart")
    public ResponseEntity<String> updateCartDetails(@Valid Principal principal) {
        customerService.emptyCart(principal.getName());
        return new ResponseEntity<>("Cart is Empty!!", HttpStatus.OK);
    }


    //Order API

    @PostMapping("/customer/orderProducts")
    public ResponseEntity<String> orderProducts(@Valid Principal principal) {
        customerService.orderProducts(principal.getName());
        return new ResponseEntity<>("Order Placed!!", HttpStatus.OK);
    }

    @PostMapping("/customer/partialOrder")
    public ResponseEntity<String> partialOrder(@RequestParam("id") int productVariationId,  @Valid Principal principal) {
        customerService.partialOrder(productVariationId,principal.getName());
        return new ResponseEntity<>("Order Placed!!", HttpStatus.OK);
    }

    @PostMapping("/customer/directOrder")
    public ResponseEntity<String> orderProductsDirectly(@RequestParam("id") int productVariationId, @RequestParam("quantity") int quantity, @Valid Principal principal) {
        customerService.orderDirectly(productVariationId,quantity,principal.getName());
        return new ResponseEntity<>("Order Placed!!", HttpStatus.OK);
    }

    @PutMapping("/customer/cancelOrder")
    public ResponseEntity<String> cancelOrder(@RequestParam("id") int orderProductId, @Valid Principal principal) {
        customerService.cancelOrder(orderProductId,principal.getName());
        return new ResponseEntity<>("Order Cancelled!!", HttpStatus.OK);
    }

    @PutMapping("/customer/returnOrder")
    public ResponseEntity<String> returnOrder(@RequestParam("id") int orderProductId, @Valid Principal principal) {
        customerService.returnOrder(orderProductId,principal.getName());
        return new ResponseEntity<>("Order Returned!!", HttpStatus.OK);
    }

    @GetMapping("/customer/viewOrder")
    public MappingJacksonValue viewOrder(@RequestParam("id") int orderId, @Valid Principal principal)
    {

        Order order= customerService.viewOrder(orderId,principal.getName());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(order);
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("id","amountPaid","paymentMethod","dateCreated","customer","product");
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("OrderFilter", filter1)
                .addFilter("Filter", filter2);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/customer/viewAllOrders")
    public MappingJacksonValue viewAllOrder(@Valid Principal principal) {
        List<Order> order= customerService.viewAllOrders(principal.getName());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(order);
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("id","amountPaid","paymentMethod","dateCreated","customer","product");
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("OrderFilter", filter1)
                .addFilter("Filter", filter2);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }
}


