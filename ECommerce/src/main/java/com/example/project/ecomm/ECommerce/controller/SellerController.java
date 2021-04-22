package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.Order.OrderProduct;
import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
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


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class SellerController
{
    @Autowired
    private SellerService sellerService;


    // Registration API

    @PostMapping("/register/seller")
    public ResponseEntity<Object> registerSeller(@Valid @RequestBody SellerDto sellerDto) {
        sellerService.register(sellerDto);
        return new ResponseEntity<>("Account successfully create", HttpStatus.OK);
    }


    // Reset Password API

    @PostMapping("/register/seller/forgetPasswordSeller")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody EmailDto emailDto)
    {
        sellerService.resendToken(emailDto);
        return new ResponseEntity<>("Link to reset password sent",HttpStatus.OK);
    }

    @PutMapping("/register/seller/confirmForgetPassword")
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

    @PutMapping("/seller/updateProfile")
   public ResponseEntity<?> updateProfile(@Valid Principal principal, @RequestBody ProfileDto profileDto)
    {
        sellerService.updateProfile(principal.getName(), profileDto);
        return new ResponseEntity<>("Profile Updated", HttpStatus.OK);
    }

    @PutMapping("/seller/updatePassword")
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

    @GetMapping("/seller/getCategory")
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

    @PostMapping("/seller/addProduct")
    public ResponseEntity<Object> addProduct(@RequestBody ProductDto productDto, Principal principal)
    {
        sellerService.addProduct(productDto,principal.getName());
        return new ResponseEntity<>("Link to Activate Product sent", HttpStatus.OK);
    }

    @GetMapping("/seller/getProduct")
    public MappingJacksonValue getProduct(@RequestParam("id") Integer id, Principal principal)
    {
        Product product = sellerService.getProduct(id, principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/seller/getAllProduct")
    public MappingJacksonValue getAllProduct(Principal principal)
    {
        List<Product> list = sellerService.getAllProducts(principal.getName());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @DeleteMapping("/seller/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam("id") Integer id, Principal principal)
    {
        sellerService.deleteProduct(id, principal.getName());
        return new ResponseEntity<>("Product Deleted", HttpStatus.OK);
    }

    @PutMapping("/seller/updateProduct")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductDto productDto, @RequestParam("id") Integer id)
    {
        sellerService.updateProduct(productDto, id);
        return new ResponseEntity<>("Product Updated", HttpStatus.OK);
    }


    @PostMapping("/seller/addProductVariation")
    public ResponseEntity<Object> addProductVariation(@RequestBody ProductVariationDto productVariationDto,Principal principal) {
        sellerService.addProductVariation(productVariationDto,principal.getName());
        return new ResponseEntity<>("Product variation has been added", HttpStatus.OK);
    }

    @GetMapping("/seller/getProductVariation")
    public MappingJacksonValue getProductVariation(@RequestParam("id") Integer productId, Principal principal)  //give productId
    {
        ProductVariation productVariation=sellerService.getProductVariationById(principal.getName(),productId);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "product", "quantityAvailable", "price", "image","metadata");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(productVariation);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @PutMapping("/seller/updateProductVariation")
    public ResponseEntity<Object> updateProductVariation(@RequestBody ProductVariationDto updateProductVariationDto,Principal principal) {
        sellerService.updateProductVariation(updateProductVariationDto,principal.getName());
        return new ResponseEntity<>("Product variation has been Updated", HttpStatus.OK);
    }

    @GetMapping("/seller/getProductVariations")                              //productId
    public MappingJacksonValue viewProductVariationByProductId(@RequestParam("id") Integer productId,Principal principal) {
        List<ProductVariation> productVariations=sellerService.getAllProductVariationForProductById(principal.getName(),productId);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "product", "quantityAvailable", "price", "image","metadata");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(productVariations);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;

    }


    //Order API

    @GetMapping("/seller/viewAllOrders")
    public MappingJacksonValue viewAllOrders(@Valid Principal principal) {
        List<OrderProduct> orderProductList = sellerService.viewAllOrders(principal.getName());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(orderProductList);
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("id", "quantity", "price", "order", "productVariation");
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("id", "amountPaid","paymentMethod");
        SimpleBeanPropertyFilter filter3 = SimpleBeanPropertyFilter.filterOutAllExcept("id");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("OrderProductFilter", filter1)
                .addFilter("OrderFilter", filter2)
                .addFilter("Filter", filter3);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PutMapping("/seller/changeStatus")
    public ResponseEntity<String> changeOrderStatus(@RequestBody StatusDto statusDto,@Valid Principal principal){
        sellerService.changeOrderStatus(statusDto,principal);
        return new ResponseEntity<>("Order Status  Changed ", HttpStatus.OK);
    }
}
