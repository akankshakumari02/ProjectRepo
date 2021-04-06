package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.ActivatedUserException;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.AddressRepository;
import com.example.project.ecomm.ECommerce.repository.SellerRepository;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import com.example.project.ecomm.ECommerce.validatorobjects.AddressDto;
import com.example.project.ecomm.ECommerce.validatorobjects.EmailDto;
import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import com.example.project.ecomm.ECommerce.validatorobjects.ProfileDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.validation.Valid;

import java.util.Arrays;

import java.util.List;

@Service
public class SellerService
{
    private SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public boolean checkIfUserExist(String email)
    {
        return sellerRepository.findByEmail(email) != null;
    }

    public void register(@Valid Seller seller) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (checkIfUserExist(seller.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        Role role = userService.getRole(UserRole.SELLER);
        seller.getRoleList().add(role);
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        seller.setRoleList(Arrays.asList(role));
        List<Address> addressList = seller.getAddressList();
        for(Address address : addressList)
        {
            address.setUser(seller);
        }
        sellerRepository.save(seller);
        userService.sendActivationLinkOfSeller(seller);
    }

    public void confirmRegistration(String token)
    {
        String info = userService.validateVerificationTokenAndSaveUser(token);
        if(info.equals("InvalidToken"))
        {
            throw new InvalidTokenException("Invalid Token");
        }
        if(info.equals("ExpiredToken"))
        {
            throw new InvalidTokenException("Expired Token");
        }
    }
    public void resendToken(EmailDto emailDto) throws UserNotFoundException {
        Seller  seller = sellerRepository.findByEmail(emailDto.getEmail());
        if (seller == null) {
            throw new UserNotFoundException("No such email found!");
        }
        if (seller.isIs_Active()) {
            userService.resetPasswordLinkSeller(seller);
        }
        else{
            throw new ActivatedUserException("Account not activated");
        }

    }

    public void resetPassword(PasswordDto passwordDto, String token)
    {
        userService.resetPassword(passwordDto, token);
    }


    public boolean emailExists(String email)
    {
        return userRepository.findByEmail(email) != null;
    }

    public User viewProfile(String email)
    {
        return userService.findUserByEmail(email);
    }

    public void updateProfile(String email, ProfileDto profileDto)
    {
        User user = userService.findUserByEmail(email);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(profileDto,user);
        userRepository.save(user);
    }

    public void updatePassword(String email, PasswordDto resetPasswordDto) {
        userService.updatePassword(email, resetPasswordDto);
    }

    public void updateAddress(AddressDto addressDto, int id) {
        userService.updateAddress(addressDto, id);
    }
}
