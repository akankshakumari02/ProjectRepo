package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.validation.EmailValidator;
import com.example.project.ecomm.ECommerce.validation.PhoneNumberValidate;
import com.example.project.ecomm.ECommerce.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto
{
    @EmailValidator.ValidEmail
    @NotNull
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
    @Size(max = 10)
    private String gst;


    private List<Long> contactList;

    @NotEmpty
    private String companyName;
    private List<Address> addressList;
}