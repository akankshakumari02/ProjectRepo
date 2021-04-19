package com.example.project.ecomm.ECommerce.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidate, PhoneNumber> {

    @Override
    public void initialize(PhoneNumberValidate constraintAnnotation) {

    }

    @Override
    public boolean isValid(PhoneNumber phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber.getLocale() == null || phoneNumber.getValue() == null) {
            return false;
        }
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber.getValue(), phoneNumber.getLocale()));
        } catch (NumberParseException e) {
            return false;
        }
    }
}