package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto
{
    @Email
    private String email;
    private String password;

}
