package com.example.project.ecomm.ECommerce.validatorobjects;


import com.example.project.ecomm.ECommerce.entities.Order.Order;
import com.example.project.ecomm.ECommerce.entities.Product.ProductReview;
import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.validation.EmailValidator;

import javax.validation.constraints.NotNull;

import com.example.project.ecomm.ECommerce.validation.PhoneNumberValidate;
import com.example.project.ecomm.ECommerce.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto
{
    @EmailValidator.ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;
    @NotNull
    @Size(min = 1, message = "First Name should have at least 1 character")
    private String firstName;
    private String middleName;
    @NotNull
    @Size(min = 1, message = "Last Name should have at least 1 character")
    private String lastName;
    @ValidPassword
    String password;
    private boolean isDeleted = false;
    private boolean isActive = false;
    private boolean accountNonLocked = true;

    @NotEmpty
    private List<Long> contactList;
    private List<Address> addressList;

    private List<ProductReview> productReviewList;
    private List<Order> orderList;
}
