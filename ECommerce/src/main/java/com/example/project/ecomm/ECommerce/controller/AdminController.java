package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.Order.Order;
import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.CategoryMetadataField;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.AdminService;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldValuesDto;
import com.example.project.ecomm.ECommerce.validatorobjects.StatusDto;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.Inet4Address;
import java.security.Principal;
import java.util.List;

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

    @GetMapping("/admin/getSeller")
    public MappingJacksonValue getSellerDetails() {
        Iterable<Seller> list = adminService.getSeller();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName","middleName","lastName","email","Is_Active","companyName","addressList","contactList");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

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

    //Category API

    @PostMapping("/admin/addCategory")
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryDto categoryDto)
    {
        String name = adminService.addCategory(categoryDto);
        return new ResponseEntity<>("Category Added", HttpStatus.OK);
    }

    @GetMapping("/admin/getCategory")
    public MappingJacksonValue getCategory(@RequestParam Integer id) //categoryId
    {
        Category category = adminService.getCategory(id);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(category);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/admin/getAllCategories")
    public MappingJacksonValue getAllCategories() {
        Iterable<Category> list = adminService.getAllCategories();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @Transactional
    @PutMapping("/admin/updateCategory")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        adminService.updateCategory(categoryDto);
        return new ResponseEntity<>("Category Updated", HttpStatus.OK);
    }


    @PostMapping("/admin/addCategoryMetadataField")
    public ResponseEntity<String> addMetadata(@RequestBody CategoryMetadataFieldDto categoryMetadataFieldDto)
   {
       String name = adminService.addCategoryMetadata(categoryMetadataFieldDto);
       return new ResponseEntity<>("Field Added....", HttpStatus.OK);
   }

   @GetMapping("/admin/getCategoryMetadataField")
    public MappingJacksonValue getMetadata()
   {
       Iterable<CategoryMetadataField> list = adminService.getCategoryMetadata();
       SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
       FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
       MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
       mappingJacksonValue.setFilters(filterProvider);
       return mappingJacksonValue;
   }


   @PostMapping("/admin/addMetadataValues")
    public ResponseEntity<Object> addMetadataValues(@RequestBody CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto)
   {
       adminService.addCategoryMetadataFieldValues(categoryMetadataFieldValuesDto);
       return new ResponseEntity<>("CategoryMetadataField Values Added", HttpStatus.OK);
   }

    @Transactional
    @PutMapping("/admin/updateCategoryMetadataFieldValues")
    public ResponseEntity<Object> updateCategoryMetadataValues(@Valid @RequestBody CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto) {
        adminService.updateCategoryMetadataFieldValues(categoryMetadataFieldValuesDto);
        return new ResponseEntity<>("Category Metadata Values Updated Successfully.", HttpStatus.OK);
    }

    //Product API

    @GetMapping("/admin/getProduct")
    public MappingJacksonValue getProduct(@RequestParam Integer id) //productId
    {
        Product product = adminService.getProduct(id);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/admin/getAllProducts")
    public MappingJacksonValue getAllProducts()
    {
        Iterable<Product> list = adminService.getAllProductDetails();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PutMapping("/admin/activateProduct/{id}")
    public ResponseEntity<String> activateProduct(@Valid @PathVariable Integer id)
    {
        return adminService.activateProduct(id);
    }

    @PutMapping("/admin/deactivateProduct/{id}")
    public ResponseEntity<String> deActivateProduct(@Valid @PathVariable Integer id)
    {
        return adminService.deActivateProduct(id);
    }

    // Order API

    @GetMapping("/admin/viewAllOrders")
    public MappingJacksonValue viewAllOrder() {
        List<Order> order= adminService.viewAllOrders();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(order);
        SimpleBeanPropertyFilter filter1 = SimpleBeanPropertyFilter.filterOutAllExcept("id","amountPaid","paymentMethod","dateCreated","customer","product");
        SimpleBeanPropertyFilter filter2 = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName");
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("OrderFilter", filter1)
                .addFilter("Filter", filter2);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PutMapping("/admin/changeStatus")
    public ResponseEntity<String> changeStatus(@RequestBody StatusDto statusDto)
    {
        adminService.changeOrderStatus(statusDto);
        return new ResponseEntity<>("Order Status Changed", HttpStatus.OK);
    }
}
