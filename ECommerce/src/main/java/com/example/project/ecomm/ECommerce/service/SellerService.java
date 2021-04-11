package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Product.*;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    public boolean checkIfUserExist(String email)
    {
        return sellerRepository.findByEmail(email) != null;
    }

    public void register(@Valid SellerDto sellerDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Seller seller=new Seller();
        if (checkIfUserExist(sellerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Already registered Email");
        }
        seller.setEmail(sellerDto.getEmail());
        seller.setFirstName(sellerDto.getFirstName());
        if(sellerDto.getMiddleName()!=null) {
            seller.setMiddleName(sellerDto.getMiddleName());
        }
        if(sellerDto.getLastName()!=null) {
            seller.setLastName(sellerDto.getLastName());
        }
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        seller.setContactList(sellerDto.getContactList());
        seller.setAddressList(sellerDto.getAddressList());
        for (Address address : seller.getAddressList()) {
            address.setUser(seller);
        }
        Role role = userService.getRole(UserRole.SELLER);
        seller.getRoleList().add(role);
        seller.setRoleList(Arrays.asList(role));
        seller.setGst(sellerDto.getGst());
        seller.setCompanyName(sellerDto.getCompanyName());
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


    public void deleteProduct(Integer id,String email) {
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            throw new RuntimeException("Product not found");
        Product product1 = product.get();
        if(seller!=product1.getSeller())
            throw new RuntimeException("This product is not listed by This Seller");
        product1.setIs_Deleted(true);
        productRepository.save(product1);
    }


    public void updateProduct(ProductDto productDto, Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.orElseThrow(() -> new RuntimeException("Product  not found"));
        productOptional.ifPresent(product -> {
            ModelMapper mm = new ModelMapper();
            mm.map(productDto, product);
            productRepository.save(product);
        });
    }

    public void addProductVariation(ProductVariationDto productVariationDto, String email){
        Integer productId = productVariationDto.getProductId();
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Product> productOptional = productRepository.findById(productId);
        productOptional.orElseThrow(() -> new RuntimeException("No product found"));
        Product product = productOptional.get();
        if (!(product.getSeller().getId()==seller.getId())|| !product.isIs_Active() || product.isIs_Deleted()) {
            throw new RuntimeException("No product found");
        }

        Category category = product.getCategory();
        List<MetadataDto>  metadataFieldValues= productVariationDto.getMetadataDto();
        validate(category, metadataFieldValues); //to check if we have added particular metadatafieldvalues in the given category

        ProductVariation productVariation = new ProductVariation();
        productVariation.setPrice(productVariationDto.getPrice());
        productVariation.setQuantityAvailable(productVariationDto.getQuantityAvailable());
        productVariation.setProduct(product);
        productVariation.setImage(productVariationDto.getImage());

        String metadata = buildMetadata(metadataFieldValues);
        productVariation.setMetadata(metadata);
        productVariationRepository.save(productVariation);
    }

    private void validate(Category category, List<MetadataDto> metadataFieldValues) {
        Integer categoryId = category.getId();
        List<Integer> categoryMetadataFieldIds = category.getCategoryMetadataFieldValuesList()
                .stream()
                .map(categoryMetadataFieldValues -> categoryMetadataFieldValues.getCategoryMetadataField().getId())
                .collect(Collectors.toList());
        List<CategoryMetadataFieldValues> categoryMetadataFieldValues = category.getCategoryMetadataFieldValuesList();
        for (MetadataDto metadataDto : metadataFieldValues) {
            int metadataFieldId = metadataDto.getId();
            if (!categoryMetadataFieldIds.contains(metadataFieldId)) {
                throw new RuntimeException("Category Metadata Field not found"); //if category metadatafield is associated with it or not
            }
            CategoryMetadataFieldValues categoryMetadataFieldValues1 = categoryMetadataFieldValues.stream()
                    .filter(categoryMetadataFieldValues2 -> Objects.equals(categoryMetadataFieldValues2.getCategory().getId(), categoryId) && Objects.equals(categoryMetadataFieldValues2.getCategoryMetadataField().getId(), metadataFieldId))
                    .findFirst().get();
            String requestValue = metadataDto.getValue();
            Optional<String> result = Arrays.stream(categoryMetadataFieldValues1.getName().split(",")).filter(value -> value.equals(requestValue)).findFirst();
            if (result.isEmpty()) {
                throw new RuntimeException("Metadata Field Value should be within the possible values");
            }
        }
    }

    private String buildMetadata(List<MetadataDto> metadataFieldIdValues) {
        StringBuilder sb = new StringBuilder("{");
        for (var metadata : metadataFieldIdValues) {
            CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findById(metadata.getId()).get();
            sb.append("\"")
                    .append(categoryMetadataField.getName())
                    .append("\":\"")
                    .append(metadata.getValue())
                    .append("\",");
        }
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }

    public ProductVariation getProductVariationById(String email, Integer productVariationId) {
        Seller seller = (Seller) userService.findUserByEmail(email);
        Optional<ProductVariation> productVariationOptional = productVariationRepository.findById(productVariationId);
        productVariationOptional.orElseThrow(() -> new RuntimeException("No product variation found"));
        ProductVariation productVariation = productVariationOptional.get();
        if (productVariation.getProduct().getSeller().getId()==(seller.getId()) && !productVariation.getProduct().isIs_Deleted()) {
            return productVariation;
        }
        throw new RuntimeException("No product variation found");
    }

    public void updateProductVariation(ProductVariationDto updateProductVariationDto, String email) {
        Integer productVariationId = updateProductVariationDto.getId();
        Seller seller = sellerRepository.findByEmail(email);
        Optional<ProductVariation> productVariationOptional = productVariationRepository.findById(productVariationId);
        productVariationOptional.orElseThrow(() -> new RuntimeException("No product variation found"));
        ProductVariation productVariation = productVariationOptional.get();
        Product product = productVariation.getProduct();

        if (!(product.getSeller().getId()==seller.getId()) || !product.isIs_Active() || product.isIs_Deleted()) {
            throw new RuntimeException("No product variation found");
        }

        Category category = product.getCategory();
        List<MetadataDto> metadataFieldIdValues = updateProductVariationDto.getMetadataDto();
        validate(category, metadataFieldIdValues);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(updateProductVariationDto, productVariation);
        String metadata = buildMetadata(metadataFieldIdValues);
        productVariation.setMetadata(metadata);
        productVariationRepository.save(productVariation);
    }

    public List<ProductVariation> getAllProductVariationForProductById(String email, Integer productId) {
        Seller seller = sellerRepository.findByEmail(email);
        Optional<Product> productOptional = productRepository.findById(productId);
        productOptional.orElseThrow(() -> new RuntimeException("No product found"));
        Product product = productOptional.get();
        if (product.getSeller().getId()==(seller.getId()) && !product.isIs_Deleted()) {
            return product.getProductVariationList();
        }
        throw new RuntimeException("No product found" );
    }
}
