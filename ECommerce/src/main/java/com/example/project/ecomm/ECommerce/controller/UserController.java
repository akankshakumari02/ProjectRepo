package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import com.example.project.ecomm.ECommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController
{
   @Autowired
    private UserService userService;

   @Autowired
   private UserRepository userRepository;

    @GetMapping("/admin/home")
    public String adminHome(){
        return "Admin home";
    }

    @GetMapping("/customer/home")
    public String customerHome(){
        return "Customer home";
    }

    @GetMapping("/seller/home")
    public String sellerHome(){
        return "Seller home";
    }

    @GetMapping
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }
}

