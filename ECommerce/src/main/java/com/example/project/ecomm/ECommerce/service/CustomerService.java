package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Order.*;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.FromStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.ToStatus;
import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.*;
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
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StatusRepository statusRepository;

    public boolean checkIfUserExist(String email)
    {
        return customerRepository.findByEmail(email) != null;
    }

    /* Registration */


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

    /* Forgot Password */

    public void resetPassword(PasswordDto passwordDto, String token)
    {
        userService.resetPassword(passwordDto, token);
    }

    public void updatePassword(String email, PasswordDto passwordDto)
    {
        userService.updatePassword(email, passwordDto);
    }


    /* Profile */

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


    /* Category */

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


    /* Product */

    public Product viewProductDetails(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            throw new FieldNotFoundException("Product Id not found.");
        Product product1 = product.get();
        if (product1.isIs_Active() == false)
            throw new ProductEntityException("Product has not been activated yet.");
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
            throw new FieldNotFoundException("Product Id not found.");
        Product product1 = product.get();
        for (Product product2 : productList) {
            if (product2.getCategory() != product1.getCategory())
                productList.remove(product2);
        }
        return productList;
    }


    /* Cart */

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
        cart.setCreatedDate(new Date(System.currentTimeMillis()));
        cartRepository.save(cart);
    }

    public List<Cart> viewCart(String email)
    {
        Customer customer = customerRepository.findByEmail(email);

        if(customer == null)
        {
            throw new UserNotFoundException("User Not Found");
        }
        return cartRepository.findById(customer.getId());
    }

    public void deleteFromCart(int productVariationId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null)
        {
            throw new UserNotFoundException("User not Found");
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
            throw new UserNotFoundException("User not Found");
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

        if(quantity == 0)
        {
            cartRepository.deleteProduct(customer.getId(),productVariationId);
        }

        int newQuantity = cart.getQuantity();
        cart.setQuantity(quantity + newQuantity);
        cart.setModifiedDate(new Date(System.currentTimeMillis()));
        cartRepository.save(cart);
    }

    public void emptyCart(String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null)
        {
            throw new UserNotFoundException("User not Found");
        }

        cartRepository.deleteAllProduct(customer.getId());
    }


    /* Order */

    public void orderProducts(String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        List<Cart> cartList = customer.getCarts();
        if(cartList.isEmpty())
        {
            throw new CartEmptyException("Cart is Empty");
        }

        Order order = new Order();
        order.setCustomer(customer);
        List<OrderProduct> orderProductList = new ArrayList<>();
        int orderAmount = 0;
        for(Cart cart : cartList)
        {
            OrderProduct orderProduct = new OrderProduct();
            OrderStatus orderStatus = new OrderStatus();
            Optional<ProductVariation> productVariation = productVariationRepository.findById(cart.getProductVariation().getId());

            if(productVariation.isEmpty())
            {
                throw new ProductEntityException("Product Variation Id is not Valid");
            }

            ProductVariation productVariation1 = productVariation.get();
            if(!productVariation1.getProduct().isIs_Active())
            {
                throw new ProductEntityException("Product In Active");
            }

            Product product = productVariation1.getProduct();

            if(product.isIs_Deleted())
            {
                throw new ProductEntityException("Product has been deleted");
            }

            if(cart.getQuantity() > productVariation1.getQuantityAvailable())
            {
                throw new ProductEntityException("Quantity is exceeding");
            }

            int amount = productVariation1.getPrice() * cart.getQuantity();
            orderAmount = orderAmount + amount;

            orderProduct.setProductVariation(productVariation1);
            orderProduct.setProductVariationMetadata(productVariation1.getMetadata());
            orderProduct.setOrder(order);
            orderProduct.setQuantity(cart.getQuantity());
            orderProduct.setPrice(amount);
            orderProductList.add(orderProduct);

            orderStatus.setOrderProduct(orderProduct);
            orderStatus.setTransitionNotesComments("Order Placed!!");
            orderStatus.setFromStatus(FromStatus.ORDER_PLACED.name());
            orderStatus.setToStatus(ToStatus.ORDER_CONFIRMED.name());
            orderProduct.setOrderStatus(orderStatus);
        }

        order.setAmountPaid(orderAmount);
        order.setPaymentMethod("Card");
        order.setDateCreated(new Date(System.currentTimeMillis()));
        order.getOrderProductList().addAll(orderProductList);
        orderRepository.save(order);
    }

    public void partialOrder(int productVariationId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Optional<Cart> cartOptional = cartRepository.findByProductVariationId(productVariationId);
        if(cartOptional.isEmpty())
        {
            throw new CartEmptyException("Cart is Empty");
        }

        Cart cart = cartOptional.get();
        Order order = new Order();
        order.setCustomer(customer);
        OrderProduct orderProduct = new OrderProduct();
        OrderStatus orderStatus = new OrderStatus();
        Optional<ProductVariation> productVariation = productVariationRepository.findById(productVariationId);

        if(productVariation.isEmpty())
        {
            throw new ProductEntityException("Product Variation Id is not Valid");
        }
        ProductVariation productVariation1 = productVariation.get();

        if(!productVariation1.getProduct().isIs_Active())
        {
            throw new ProductEntityException("Product In Active");
        }

        Product product = productVariation1.getProduct();

        if(product.isIs_Deleted())
        {
            throw new ProductEntityException("Product has been deleted");
        }

        if(cart.getQuantity() > productVariation1.getQuantityAvailable())
        {
            throw new ProductEntityException("Quantity is exceeding");
        }

        int amount = productVariation1.getPrice() * cart.getQuantity();

        orderProduct.setProductVariation(productVariation1);
        orderProduct.setProductVariationMetadata(productVariation1.getMetadata());
        orderProduct.setOrder(order);
        orderProduct.setQuantity(cart.getQuantity());
        orderProduct.setPrice(amount);


        orderStatus.setOrderProduct(orderProduct);
        orderStatus.setTransitionNotesComments("Order Placed!!");
        orderStatus.setFromStatus(FromStatus.ORDER_PLACED.name());
        orderStatus.setToStatus(ToStatus.ORDER_CONFIRMED.name());
        orderProduct.setOrderStatus(orderStatus);

        order.setAmountPaid(amount);
        order.setPaymentMethod("Card");
        order.setDateCreated(new Date(System.currentTimeMillis()));
        order.getOrderProductList().add(orderProduct);
        orderRepository.save(order);
    }

    public void orderDirectly(int productVariationId, int orderQuantity, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Order order = new Order();
        order.setCustomer(customer);
        OrderProduct orderProduct = new OrderProduct();
        OrderStatus orderStatus = new OrderStatus();

        Optional<ProductVariation> productVariation = productVariationRepository.findById(productVariationId);

        if(productVariation.isEmpty())
        {
            throw new ProductEntityException("Product Variation Id is not Valid");
        }
        ProductVariation productVariation1 = productVariation.get();

        if(!productVariation1.getProduct().isIs_Active())
        {
            throw new ProductEntityException("Product Is Not Active");
        }

        Product product = productVariation1.getProduct();

        if(product.isIs_Deleted())
        {
            throw new ProductEntityException("Product has been deleted");
        }

        if(orderQuantity > productVariation1.getQuantityAvailable())
        {
            throw new ProductEntityException("Quantity is exceeding than the existing one");
        }

        int amount = productVariation1.getPrice() * orderQuantity;
        orderProduct.setProductVariation(productVariation1);
        orderProduct.setProductVariationMetadata(productVariation1.getMetadata());
        orderProduct.setOrder(order);
        orderProduct.setQuantity(orderQuantity);
        orderProduct.setPrice(amount);


        orderStatus.setOrderProduct(orderProduct);
        orderStatus.setTransitionNotesComments("Order Placed!!");
        orderStatus.setFromStatus(FromStatus.ORDER_PLACED.name());
        orderStatus.setToStatus(ToStatus.ORDER_CONFIRMED.name());
        orderProduct.setOrderStatus(orderStatus);

        order.setAmountPaid(amount);
        order.setPaymentMethod("Card");
        order.setDateCreated(new Date(System.currentTimeMillis()));
        order.getOrderProductList().add(orderProduct);
        orderRepository.save(order);
    }


    public void cancelOrder(int orderProductId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);

        if(orderProduct.isEmpty())
        {
            throw new OrderEntityException("Invalid OrderProduct Id");
        }
        OrderProduct orderProduct1 = orderProduct.get();

        Optional<OrderStatus> orderStatus = statusRepository.findById(orderProductId);

        if(orderStatus.isEmpty())
        {
            throw new OrderEntityException("OrderStatus does not exists");
        }
        OrderStatus orderStatus1 = orderStatus.get();

        if(!orderProduct1.getOrderStatus().getFromStatus().equals(FromStatus.ORDER_PLACED.name()))
        {
            throw new OrderEntityException("Order not Placed");
        }
        Optional<Order> order = orderRepository.findById(orderProduct1.getOrder().getId());
        if(order.isEmpty())
        {
            throw new OrderEntityException("Order does not exist");
        }

        Order order1 = order.get();
        if(order1.getCustomer().getId() != customer.getId())
        {
            throw new OrderEntityException("Order does not belong to this Customer");
        }

        orderStatus1.setTransitionNotesComments("Order Cancelled");
        orderStatus1.setToStatus(ToStatus.CANCELLED.name());
        statusRepository.save(orderStatus1);
    }

    public void returnOrder(int orderProductId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        if(orderProduct.isEmpty())
        {
            throw new OrderEntityException("OrderStatus does not exists");
        }
        OrderProduct orderProduct1 = orderProduct.get();

        Optional<OrderStatus> orderStatus = statusRepository.findById(orderProductId);

        if(orderStatus.isEmpty())
        {
            throw new OrderEntityException("OrderStatus does not exists");
        }
        OrderStatus orderStatus1 = orderStatus.get();

        if (!orderProduct1.getOrderStatus().getFromStatus().equals(FromStatus.DELIVERED.name()))
        {
            throw new OrderEntityException("Order is not Delivered Yet");
        }

        Optional<Order> order = orderRepository.findById(orderProduct1.getOrder().getId());
        if(order.isEmpty())
        {
            throw new OrderEntityException("Orderd Does not exists");
        }

        Order order1 = order.get();
        if(order1.getCustomer().getId() != customer.getId())
        {
            throw new OrderEntityException("Order does not belong to this Customer");
        }

        orderStatus1.setTransitionNotesComments("Return Request");
        orderStatus1.setToStatus(ToStatus.RETURN_REQUESTED.name());
        statusRepository.save(orderStatus1);
    }

    public Order viewOrder(int orderId, String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty())
        {
            throw new OrderEntityException("Invalid OrderId");
        }

        Order order1 = order.get();
        if(order1.getCustomer().getId() != customer.getId())
        {
            throw new OrderEntityException("Order does not belong to this customer");
        }

        return order1;
    }

    public List<Order> viewAllOrders(String email)
    {
        Customer customer = customerRepository.findByEmail(email);
        Pageable pageable = PageRequest.of(0,10,Sort.by("id"));
        Page<Order> orders = orderRepository.findByCustomerId(customer.getId(), pageable);
        if(orders.hasContent())
        {
            return orders.getContent();
        }
        else
        {
            return new ArrayList<Order>();
        }
    }
}
