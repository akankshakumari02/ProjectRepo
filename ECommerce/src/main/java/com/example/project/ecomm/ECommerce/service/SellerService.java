package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Role;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.repository.CustomerRepository;
import com.example.project.ecomm.ECommerce.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;

@Service
public class SellerService
{
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    public boolean checkIfUserExist(String email)
    {
        return sellerRepository.findByEmail(email) != null;
    }

    public void register(@Valid Seller seller) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (checkIfUserExist(seller.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        Role role = new Role();
        role.setAuthority("ROLE_SELLER");
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        seller.setRoleList(Arrays.asList(role));

        Address address= seller.getAddress();
        address.setSeller(seller);

        sellerRepository.save(seller);
        userService.sendActivationLinkWithSeller(seller);
    }
}
