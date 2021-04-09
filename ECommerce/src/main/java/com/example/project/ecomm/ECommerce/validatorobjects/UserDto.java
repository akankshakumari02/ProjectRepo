package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.validation.EmailValidator;
import com.example.project.ecomm.ECommerce.validation.PasswordMatch;
import com.example.project.ecomm.ECommerce.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@PasswordMatch
public class UserDto {
    @NotNull
    @Size(min = 1)
    private String firstName;

    @NotNull
    @Size(min = 1)
    private String lastName;

    private String middleName;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchPassword;

    @EmailValidator.ValidEmail
    @NotNull
    @Size(min = 1, message = "Email should be well-formed")
    private String email;

}
