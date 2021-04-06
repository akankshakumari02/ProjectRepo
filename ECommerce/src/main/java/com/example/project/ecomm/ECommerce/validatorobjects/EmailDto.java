package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.validation.EmailValidator;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class EmailDto
{
    @EmailValidator.ValidEmail
    @NotNull
    @Size(min = 1, message = "Email should be well-formed")
    private String email;
}
