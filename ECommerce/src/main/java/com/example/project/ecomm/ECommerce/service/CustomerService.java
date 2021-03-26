package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Role;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.repository.CustomerRepository;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@Service
public class CustomerService
{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    public boolean checkIfUserExist(String email)
    {
        return customerRepository.findByEmail(email) != null;
    }

   /* public void register(@Valid Customer customer) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        *//*if (checkIfUserExist(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");*//**//*
        }*//*
        Role role = new Role();
        role.setAuthority("ROLE_CUSTOMER");
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRoleList(Arrays.asList(role));
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            address.setCustomer(customer);
        }

        customerRepository.save(customer);
        userService.sendActivationLinkWithCustomer(customer);
    }*/


    public void register(@Valid Customer customer) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       /* if (checkIfUserExist(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }*/
        Role role = new Role();
        role.setAuthority("ROLE_CUSTOMER");
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRoleList(Arrays.asList(role));
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            address.setCustomer(customer);
        }
        customerRepository.save(customer);
        userService.sendActivationLinkWithCustomer(customer);
    }
}
