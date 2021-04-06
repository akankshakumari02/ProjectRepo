 package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.CustomerRepository;
import com.example.project.ecomm.ECommerce.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService
{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

/*
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
*/


    public Iterable<Customer> getCustomer()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Customer> allCustomers = customerRepository.findAll(pageable);
        return allCustomers;
    }

    public Iterable<Seller> getSeller()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Seller> allSellers = sellerRepository.findAll(pageable);
        return allSellers;
    }

    public ResponseEntity<String> activateCustomer(int id)
    {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent())
        {
            Customer customer1 = customer.get();
            if(!customer1.isIs_Active())
            {
                customer1.setIs_Active(true);
                customerRepository.save(customer1);
                userService.sendLinkWithActivationMessage(customer1);
                return ResponseEntity.ok("Account is activated");
            }
            else
            {
                return ResponseEntity.ok("Account already active");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> deActivateCustomer(int id)
    {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent())
        {
            Customer customer1 = customer.get();
            if(customer1.isIs_Active())
            {
                customer1.setIs_Active(false);
                customerRepository.save(customer1);
                userService.sendLinkWithDeActivationMessage(customer1);
                return ResponseEntity.ok("Account is DeActivated");
            }
            else
            {
                return ResponseEntity.ok("Account already DeActive");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> activateSeller(int id)
    {
        Optional<Seller> seller = sellerRepository.findById(id);
        if(seller.isPresent())
        {
            Seller seller1 = seller.get();
            if(!seller1.isIs_Active())
            {
                seller1.setIs_Active(true);
                sellerRepository.save(seller1);
                userService.sendLinkWithActivationMessage(seller1);
                return ResponseEntity.ok("Account is activated");
            }
            else
            {
                return ResponseEntity.ok("Account already active");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> deActivateSeller(int id)
    {
        Optional<Seller> seller = sellerRepository.findById(id);
        if(seller.isPresent())
        {
            Seller seller1 = seller.get();
            if(seller1.isIs_Active())
            {
                seller1.setIs_Active(false);
                sellerRepository.save(seller1);
                userService.sendLinkWithDeActivationMessage(seller1);
                return ResponseEntity.ok("Account is DeActivated");
            }
            else
            {
                return ResponseEntity.ok("Account already DeActive");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }


    /*public boolean checkIfMetadataFieldExist(String name) {
        return categoryMetadataFieldRepository.findByName(name) != null;
    }

    public String addCategoryMetadataFiled(CategoryMetadataFieldDto categoryMetadataFieldDto) {
        if (checkIfMetadataFieldExist(categoryMetadataFieldDto.getName()))
            throw new RuntimeException("CategoryMetadataField Already exists");

        CategoryMetadataField categoryMetadataField = new CategoryMetadataField(categoryMetadataFieldDto.getName());
        categoryMetadataFieldRepository.save(categoryMetadataField);
        return categoryMetadataField.getName();
    }

    public Iterable<CategoryMetadataField> getCategoryMetadataFiled() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<CategoryMetadataField> allList = categoryMetadataFieldRepository.findAll(pageable);
        return allList;
    }*/
}
