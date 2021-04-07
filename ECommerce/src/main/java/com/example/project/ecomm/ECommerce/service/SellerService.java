package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.ActivatedUserException;
import com.example.project.ecomm.ECommerce.exception.EmailAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.validatorobjects.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

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


    /* Category */

    public Iterable<Category> getAllCategory()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Category> page = categoryRepository.findAll(pageable);
        return page;
    }

    /* Product */

    @Transactional
    public void addProduct(ProductDto productDto, String email) {
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Category> category = categoryRepository.findById(productDto.getCategoryId());
        if (!category.isPresent()) {
            throw new RuntimeException("Category Id not found.");
        }
        Category category1 = category.get();
        Product product = new Product();
        ModelMapper mm = new ModelMapper();
        mm.map(productDto, product);
        category1.setProductList(List.of(product));
        seller.setProductList(List.of(product));
        product.setCategory(category1);
        product.setSeller(seller);
        productRepository.save(product);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo("aakanksha021997@gmail.com");
        mailMessage.setSubject("Complete Activation for product!");
        mailMessage.setText("Activate Product: "+ "http://localhost:8080/admin/activateproduct/"+product.getId());
        emailService.sendEmail(mailMessage);
    }



    public Product getProduct(Integer id, String email)
    {
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent())
        {
            throw new RuntimeException("Product not found");
        }
        Product product1 = product.get();
        if(seller != product1.getSeller())
        {
            throw new RuntimeException("This Product is not Listed by This Seller");
        }
        return product1;
    }


    public List<Product> getAllProducts(String email) {
        Seller seller=sellerRepository.findByEmail(email);
        List<Product> productList=seller.getProductList();
        return  productList;
    }


    public void deleteproduct(Integer id,String email) {
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            throw new RuntimeException("Product not found");
        Product product1 = product.get();
        if(seller!=product1.getSeller())
            throw new RuntimeException("This product is not listed by This Seller Cannot be Deleted");
        productRepository.delete(product1);
    }

    public void updateProduct(ProductDto productDto, Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.orElseThrow(() -> new RuntimeException("Product  not found "));
        productOptional.ifPresent(product -> {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(productDto,product);
            productRepository.save(product);
        });
    }
}
