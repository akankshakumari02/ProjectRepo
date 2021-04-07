package com.example.project.ecomm.ECommerce.validation;

import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import com.example.project.ecomm.ECommerce.validatorobjects.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public void initialize(final PasswordMatch passwordMatch)
    {

    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final PasswordDto user = (PasswordDto) obj;
        return user.getPassword().equals(user.getMatchPassword());
    }

}
