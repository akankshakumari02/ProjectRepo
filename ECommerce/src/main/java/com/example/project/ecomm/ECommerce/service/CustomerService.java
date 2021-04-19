package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import com.example.project.ecomm.ECommerce.entities.Order.CustomerProductVariationKey;
import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.*;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.token.ConfirmationToken;
import com.example.project.ecomm.ECommerce.validatorobjects.*;
import org.modelmapper.ModelMapper;
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


    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final CartRepository cartRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService, TokenRepository tokenRepository, AddressRepository addressRepository, UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, ProductRepository productRepository, ProductVariationRepository productVariationRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productVariationRepository = productVariationRepository;
        this.cartRepository = cartRepository;
    }


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
       // customer.setPassword(passwordEncoder.encode(customer.getPassword()));
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
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        if(categoryPage.hasContent())
        {
            return categoryPage.getContent();
        }
        else
        {
            return new ArrayList<Category>();
        }
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
        if(productList.hasContent())
        {
            return productList.getContent();
        }
        else {
            return new ArrayList<Product>();
        }
    }

    public List<Product> viewProductDetailsFiltered(Integer id) {
        List<Product> productList = productRepository.findAll();
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new RuntimeException("Product Id not found.");
        Product product1 = product.get();
        for (Product product2 : productList) {
            if (product2.getCategory() != product1.getCategory())
                productList.remove(product2);
        }
        return productList;
    }

    public void addToCart(int productVariationId, int quantity, String email)
    {
        Customer customer = customerRepository.findByEmail(email);

        if(customer == null)
        {
            throw new ProductEntityException("User Not Found");
        }

        Optional<ProductVariation> productVariation = productVariationRepository.findById(productVariationId);
        if(productVariation.isEmpty())
        {
            throw new ProductEntityException("Product not Found");
        }

        ProductVariation productVariation1 = productVariation.get();
        if(!productVariation1.getProduct().isIs_Active())
        {
            throw new ProductEntityException("Product is not Active");
        }

        Product product = productVariation1.getProduct();
        if(product.isIs_Deleted())
        {
            throw new ProductEntityException("Product is Deleted");
        }

        if(productVariation1.getQuantityAvailable() <=0 || productVariation1.getQuantityAvailable()<quantity)
        {
            throw new ProductEntityException("Product is not Available or the Quantity entered is more than the quantity entered");
        }

        Optional<Cart> cartOptional = cartRepository.findByProductVariationId(productVariationId);
        if(cartOptional.isPresent())
        {
            throw new ProductEntityException("Product Already Exists");
        }

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setProductVariation(productVariation1);
        CustomerProductVariationKey customerProductVariationKey = new CustomerProductVariationKey(customer.getId(),productVariationId);
        cart.setCustomerProductVariationKey(customerProductVariationKey);
        cart.setQuantity(quantity);
        cart.setWishlistItem(true);
        cartRepository.save(cart);
    }

    public List<Cart> viewCart(String email)
    {
        Customer customer = customerRepository.findByEmail(email);

        if(customer == null)
        {
            throw new RuntimeException("User Not Found");
        }
        return cartRepository.findById(customer.getId());
    }

    public void deleteFromCart(int productVariationId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null)
        {
            throw new RuntimeException("User not Found");
        }

        Optional<Cart> cartOptional = cartRepository.findByProductVariationId(productVariationId);
        if(cartOptional.isEmpty())
        {
            throw new ProductEntityException("Product is not present in the Cart");
        }

        Cart cart = cartOptional.get();
        if(cart.getCustomer().getId() != customer.getId())
        {
            throw new ProductEntityException("Product was not added by this customer");
        }

        cartRepository.deleteProduct(customer.getId(),productVariationId);
    }

    public void updateCart(int productVariationId , int quantity, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null)
        {
            throw new RuntimeException("User not Found");
        }

        Optional<ProductVariation> productVariation = productVariationRepository.findById(productVariationId);
        if(productVariation.isEmpty())
        {
            throw new ProductEntityException("Product not Found");
        }

        ProductVariation productVariation1 = productVariation.get();
        if(!productVariation1.getProduct().isIs_Active())
        {
            throw new ProductEntityException("Product is not Active");
        }

        Product product = productVariation1.getProduct();
        if(product.isIs_Deleted())
        {
            throw new ProductEntityException("Product was deleted");
        }

        if(productVariation1.getQuantityAvailable() <=0 || productVariation1.getQuantityAvailable() < quantity)
        {
            throw new ProductEntityException("Product is not Available or the Quantity entered is more than the quantity entered");
        }

        Optional<Cart> cartOptional = cartRepository.findByProductVariationId(productVariationId);
        if(cartOptional.isEmpty())
        {
            throw new ProductEntityException("Product Not Present in the Cart");
        }

        Cart cart = cartOptional.get();

        if(cart.getCustomer().getId() != customer.getId())
        {
            throw new ProductEntityException("Product was not added by this customer");
        }

        int newQuantity = cart.getQuantity();
        cart.setQuantity(quantity + newQuantity);
        cartRepository.save(cart);
    }

    public void emptyCart(String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null)
        {
            throw new RuntimeException("User not Found");
        }

        cartRepository.deleteAllProduct(customer.getId());
    }
}
