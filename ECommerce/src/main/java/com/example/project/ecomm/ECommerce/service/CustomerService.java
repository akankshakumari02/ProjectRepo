package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.AddressNotFoundException;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.token.ConfirmationToken;
import com.example.project.ecomm.ECommerce.validatorobjects.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

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
    private CategoryRepository categoryRepository;

     @Autowired
     private ProductRepository productRepository;


    public boolean checkIfUserExist(String email)
    {
        return customerRepository.findByEmail(email) != null;
    }


    public void register(@Valid CustomerDto customerDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer();
        if (checkIfUserExist(customerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }
        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        if(customerDto.getMiddleName()!=null) {
            customer.setMiddleName(customerDto.getMiddleName());
        }
        if(customerDto.getLastName()!=null) {
            customer.setLastName(customerDto.getLastName());
        }
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setContactList(customerDto.getContactList());
        customer.setAddressList(customerDto.getAddressList());
        for (Address address : customer.getAddressList()) {
            address.setUser(customer);
        }
        Role role = userService.getRole(UserRole.CUSTOMER); //will fetch data from UserRole where enum are defined
        customer.getRoleList().add(role);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRoleList(Arrays.asList(role));

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

    public Iterable<Category> getAllCategory() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Category> allCategories = categoryRepository.findAll(pageable);
        return allCategories;
    }

    public Product viewProductDetails(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            throw new RuntimeException("Product Id not found.");
        Product product1 = product.get();
        if (product1.isIs_Active() == false)
            throw new RuntimeException("Product has not been activated yet.");
        return product1;
    }

    public Iterable<Product> viewAllProductDetails() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Product> productList = productRepository.findAll(pageable);
        return productList;
    }

    public List<Product> viewProductDetailsFiltered(Integer id) {
        List<Product> productList = productRepository.findAll();
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            throw new RuntimeException("Product Id not found.");
        Product product1 = product.get();
        for (Product product2 : productList) {
            if (product2.getCategory() != product1.getCategory())
                productList.remove(product2);
        }
        return productList;
    }

}
