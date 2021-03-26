package com.example.project.ecomm.ECommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController
{
   @Autowired
    private TokenStore tokenStore;

   @GetMapping("/doLogout")
    public String logout(HttpServletRequest request)
   {
       String authHeader = request.getHeader("Authorization");
       if(authHeader != null)
       {
           String tokenValue = authHeader.replace("Bearer", "").trim();
           OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
           tokenStore.removeAccessToken(accessToken);
       }
       return "Logged out Successfully";
   }

    @GetMapping("/")
    public String index(){
        return "index";
    }

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
}

