package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PhoneNumber
{
    @NotEmpty
    private String value;

    @NotEmpty
    private String locale;
}
