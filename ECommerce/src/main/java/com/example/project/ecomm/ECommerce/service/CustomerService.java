package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.AddressNotFoundException;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.validatorobjects.AddressDto;
import com.example.project.ecomm.ECommerce.validatorobjects.EmailDto;
import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import com.example.project.ecomm.ECommerce.validatorobjects.ProfileDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;


@Service
public class CustomerService
{

    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean checkIfUserExist(String email)
    {
        return customerRepository.findByEmail(email) != null;
    }


    public void register(@Valid Customer customer) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (checkIfUserExist(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        Role role = new Role();
        role.setAuthority("ROLE_CUSTOMER");
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRoleList(Arrays.asList(role));
        List<Address> addressList = customer.getAddressList();
        for (Address address : addressList) {
            address.setUser(customer);
        }
        customerRepository.save(customer);
        userService.sendActivationLinkOfCustomer(customer);
    }


    public void confirmRegistration(String token)
    {
        String result = userService.validateUser(token);
        if(result.equals("InvalidToken"))
        {
            throw new InvalidTokenException("Invalid Token");
        }
        if(result.equals("ExpiredToken"))
        {
            throw new InvalidTokenException("Expired Token");
        }
    }

    public void resendToken(EmailDto emailDto)
    {
        Customer customer = customerRepository.findByEmail(emailDto.getEmail());
        if (customer == null) {
            throw new UserNotFoundException("This email couldn't be found");
        }
        if (customer.isIs_Active()) {
            userService.resetPasswordLinkCustomer(customer);
        }
        else
            {
                ConfirmationToken previousToken = tokenRepository.findTokenByUserId(customer.getId());
                tokenRepository.delete(previousToken);
                userService.sendActivationLinkOfCustomer(customer);
            }
    }

    public void resetPassword(PasswordDto passwordDto, String token)
    {
        userService.resetPassword(passwordDto, token);
    }

    public void updatePassword(String email, PasswordDto passwordDto)
    {
        userService.updatePassword(email, passwordDto);
    }

    public void updateProfile(Customer customer)
    {
        customerRepository.save(customer);
    }
    public User viewProfile(String email)
    {
        return userRepository.findByEmail(email);
    }

    public List<Address> addressList(String email)
    {
        User user = userService.findUserByEmail(email);
        return addressRepository.findByUser(user);
    }

    public void updateProfile(String email, ProfileDto profileDto)
    {
        User user = userService.findUserByEmail(email);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(profileDto,user);
        userRepository.save(user);
    }

    public void addAddress(String email, AddressDto addressDto)
    {
        User user = userRepository.findByEmail(email);
        Address address = new Address();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(addressDto,address);
        address.setUser(user);
        user.getAddressList().add(address);
        userRepository.save(user);
    }

    public void deleteAddress(int id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        addressOptional.orElseThrow(() -> new AddressNotFoundException("Address not found"));
        addressOptional.ifPresent(address -> addressRepository.delete(address));

    }

    public void updateAddress(AddressDto addressDto, int id) {
        userService.updateAddress(addressDto, id);
    }
}
