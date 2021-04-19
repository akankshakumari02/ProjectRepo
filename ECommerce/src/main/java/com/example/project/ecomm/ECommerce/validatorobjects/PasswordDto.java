package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.validation.PasswordMatch;
import com.example.project.ecomm.ECommerce.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatch
public class PasswordDto
{
    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchPassword;
}
