package com.example.project.ecomm.ECommerce.JWT;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class AuthenticationCredentials
{
    @Email
    private String email;
    private String password;
}
