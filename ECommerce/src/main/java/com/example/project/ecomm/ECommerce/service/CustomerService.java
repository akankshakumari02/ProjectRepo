package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.AddressNotFoundException;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.token.ConfirmationToken;
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
        Role role = userService.getRole(UserRole.CUSTOMER); //will fetch data from UserRole where enum are defined
        customer.getRoleList().add(role);
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
        String result = userService.validateVerificationTokenAndSaveUser(token);
        if(result.equals("InvalidToken"))
        {
            throw new InvalidTokenException("Invalid Token");
        }
        if(result.equals("ExpiredToken"))
        {
            throw new InvalidTokenException("Expired Token");
        }
    }

    public void resendToken(EmailDto emailDto) {
        Customer customer = customerRepository.findByEmail(emailDto.getEmail());
        if (customer == null) {
            throw new UserNotFoundException("No such email found!");
        }
        if (customer.isIs_Active()) {
            userService.sendResetPasswordMessage(customer);
        } else {
            ConfirmationToken oldToken = tokenRepository.findTokenByUserId(customer.getId());
            tokenRepository.delete(oldToken);
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

    public void updateCustomerProfile(Customer customer)
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
