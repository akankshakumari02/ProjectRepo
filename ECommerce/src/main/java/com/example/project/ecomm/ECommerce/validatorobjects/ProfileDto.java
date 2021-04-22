package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProfileDto
{
    @NotNull
    @Size(min = 1, message = "Name should have atleast 1 character")
    private String firstName;

    private String middleName;

    @Size(min = 1, message = "Should have atleast 1 character")
    private String lastName;

}
