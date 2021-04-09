package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.CategoryMetadataField;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.AdminService;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldValuesDto;
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

    //Category Api's

   @PostMapping("/admin/addcategorymetadatafied")
    public ResponseEntity<String> addMetadata(@RequestBody CategoryMetadataFieldDto categoryMetadataFieldDto)
   {
       String name = adminService.addCategoryMetadata(categoryMetadataFieldDto);
       return new ResponseEntity<>("Field Added....", HttpStatus.OK);
   }

   @GetMapping("/admin/getcategorymetadatafield")
    public MappingJacksonValue getMetadata()
   {
       Iterable<CategoryMetadataField> list = adminService.getCategoryMetadata();
       SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
       FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
       MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
       mappingJacksonValue.setFilters(filterProvider);
       return mappingJacksonValue;
   }

   @PostMapping("/admin/addcategory")
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryDto categoryDto)
   {
       String name = adminService.addCategory(categoryDto);
       return new ResponseEntity<>("Category Added", HttpStatus.OK);
   }

   @GetMapping("/admin/getcategory")
   public MappingJacksonValue getCategory(@RequestParam Integer id)
   {
       Category category = adminService.getCategory(id);
       SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
       FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
       MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(category);
       mappingJacksonValue.setFilters(filterProvider);
       return mappingJacksonValue;
   }

   @GetMapping("/admin/getallcategories")
   public MappingJacksonValue getAllCategories() {
       Iterable<Category> list = adminService.getAllCategories();
       SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");
       FilterProvider filters = new SimpleFilterProvider().addFilter("Filter", filter);
       MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
       mappingJacksonValue.setFilters(filters);
       return mappingJacksonValue;
   }

   @Transactional
    @PutMapping("/admin/updatecategory")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryDto categoryDto)
   {
       adminService.updateCategory(categoryDto);
       return new ResponseEntity<>("Category Updated", HttpStatus.OK);
   }

   @PostMapping("/admin/addmetadatavalues")
    public ResponseEntity<Object> addMetadataValues(@RequestBody CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto)
   {
       adminService.addCategoryMetadataFieldValues(categoryMetadataFieldValuesDto);
       return new ResponseEntity<>("CategoryMetadataField Values Added", HttpStatus.OK);
   }

    @Transactional
    @PutMapping("/admin/updatecategorymetadatafieldvalues")
    public ResponseEntity<Object> updateCategoryMetadataValues(@Valid @RequestBody CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto) {
        adminService.updateCategoryMetadataFieldValues(categoryMetadataFieldValuesDto);
        return new ResponseEntity<>("Category Metadata Values Updated Successfully.", HttpStatus.OK);
    }

    //Product Api's

    @GetMapping("/admin/getproduct")
    public MappingJacksonValue getProduct(@RequestParam Integer id)
    {
        Product product = adminService.getProduct(id);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(product);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/admin/getallproducts")
    public MappingJacksonValue getAllProducts()
    {
        Iterable<Product> list = adminService.getAllProductDetails();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name", "description","brand","category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Filter",filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PutMapping("/admin/activateproduct/{id}")
    public ResponseEntity<String> activateProduct(@Valid @PathVariable Integer id)
    {
        return adminService.activateProduct(id);
    }

    @PutMapping("/admin/deactivateproduct/{id}")
    public ResponseEntity<String> deActivateProduct(@Valid @PathVariable Integer id)
    {
        return adminService.deActivateProduct(id);
    }
}
