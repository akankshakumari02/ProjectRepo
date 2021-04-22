 package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Order.Order;
import com.example.project.ecomm.ECommerce.entities.Order.OrderProduct;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.FromStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusMap;
import com.example.project.ecomm.ECommerce.entities.Product.*;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.exception.*;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldValuesDto;
import com.example.project.ecomm.ECommerce.validatorobjects.StatusDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService
{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private OrderStatusMap orderStatusMap;

    public Iterable<Customer> getCustomer()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Customer> customers = customerRepository.findAll(pageable);
        if(customers.hasContent())
        {
            return customers.getContent();
        }
        else
        {
            return new ArrayList<Customer>();
        }
    }

    public Iterable<Seller> getSeller()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Seller> sellers = sellerRepository.findAll(pageable);
        if(sellers.hasContent())
        {
            return sellers.getContent();
        }
        else
        {
            return new ArrayList<Seller>();
        }
    }

    public ResponseEntity<String> activateCustomer(int id)
    {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent())
        {
            Customer customer1 = customer.get();
            if(!customer1.isIs_Active())
            {
                customer1.setIs_Active(true);
                customerRepository.save(customer1);
                userService.sendLinkWithActivationMessage(customer1);
                return ResponseEntity.ok("Account is activated");
            }
            else
            {
                return ResponseEntity.ok("Account is already active");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> deActivateCustomer(int id)
    {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent())
        {
            Customer customer1 = customer.get();
            if(customer1.isIs_Active())
            {
                customer1.setIs_Active(false);
                customerRepository.save(customer1);
                userService.sendLinkWithDeActivationMessage(customer1);
                return ResponseEntity.ok("Account is DeActivated");
            }
            else
            {
                return ResponseEntity.ok("Account already DeActive");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> activateSeller(int id)
    {
        Optional<Seller> seller = sellerRepository.findById(id);
        if(seller.isPresent())
        {
            Seller seller1 = seller.get();
            if(!seller1.isIs_Active())
            {
                seller1.setIs_Active(true);
                sellerRepository.save(seller1);
                userService.sendLinkWithActivationMessage(seller1);
                return ResponseEntity.ok("Account is activated");
            }
            else
            {
                return ResponseEntity.ok("Account already active");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    public ResponseEntity<String> deActivateSeller(int id)
    {
        Optional<Seller> seller = sellerRepository.findById(id);
        if(seller.isPresent())
        {
            Seller seller1 = seller.get();
            if(seller1.isIs_Active())
            {
                seller1.setIs_Active(false);
                sellerRepository.save(seller1);
                userService.sendLinkWithDeActivationMessage(seller1);
                return ResponseEntity.ok("Account is DeActivated");
            }
            else
            {
                return ResponseEntity.ok("Account already DeActive");
            }
        }
        else
        {
            throw new UserNotFoundException("Account not found");
        }
    }

    /* Category */

    public boolean ifCategoryExists(String name)
    {
        return categoryRepository.findByName(name) != null || parentCategoryRepository.findByName(name) != null;
    }

    public String addCategory(CategoryDto categoryDto) {
        if (ifCategoryExists(categoryDto.getName())) {
            throw new CategoryAlreadyExistsException("Category Already Exists");
        }

        if(categoryDto.getParentId() == null)
        {
            ParentCategory parentCategory = new ParentCategory(categoryDto.getName());
            parentCategoryRepository.save(parentCategory);
            return parentCategory.getName();
        }

        else
        {
            Category category = new Category(categoryDto.getName());
            Optional<ParentCategory> parentCategory = parentCategoryRepository.findById(categoryDto.getParentId());
            parentCategory.orElseThrow(() -> new CategoryAlreadyExistsException("Parent Id not Found"));

            parentCategory.ifPresent(parentCategory1 -> {
                parentCategory1.getCategoryList().add(category);
                category.setParentCategory(parentCategory1);
                categoryRepository.save(category);
            });
            return category.getName();
        }
    }

    public Category getCategory(Integer id)
    {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty())
        {
            throw new FieldNotFoundException("Category Not Found");
        }
        Category category1 = category.get();
        return category1;
    }

    public Iterable<Category> getAllCategories()
    {
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

    public void updateCategory(CategoryDto categoryDto)
    {
        if (ifCategoryExists(categoryDto.getName())) {
            throw new CategoryAlreadyExistsException("Category Already Exists");
        }
        Optional<Category> category = categoryRepository.findById(categoryDto.getId());
        category.orElseThrow(() -> new FieldNotFoundException("Category Not Found"));
        category.ifPresent(category1 -> {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(categoryDto,category1);
            categoryRepository.save(category1);
        });
    }

    public boolean ifMetadataFieldExist(String name) {
        return categoryMetadataFieldRepository.findByName(name) != null;
    }

    public String addCategoryMetadata(CategoryMetadataFieldDto categoryMetadataFieldDto) {
        if (ifMetadataFieldExist(categoryMetadataFieldDto.getName()))
            throw new CategoryMetadataFieldAlreadyExists("CategoryMetadataField Already exists");

        CategoryMetadataField categoryMetadataField = new CategoryMetadataField(categoryMetadataFieldDto.getName());
        categoryMetadataFieldRepository.save(categoryMetadataField);
        return categoryMetadataField.getName();
    }

    public Iterable<CategoryMetadataField> getCategoryMetadata() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<CategoryMetadataField> allList = categoryMetadataFieldRepository.findAll(pageable);
        return allList;
    }

    private boolean ifCategoryMetadataFieldValuesExists(Integer categoryMetadataFieldId, Integer categoryId)
    {
        return categoryMetadataFieldValuesRepository.findById(categoryMetadataFieldId, categoryId) != null;
    }

    public void addCategoryMetadataFieldValues(CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto)
    {
        if(ifCategoryMetadataFieldValuesExists(categoryMetadataFieldValuesDto.getCategoryMetadataFieldId(), categoryMetadataFieldValuesDto.getCategoryId()))
        {
            throw new CategoryMetadataFieldAlreadyExists("CategoryMetadata Field Values With Same Info Already Exists");
        }

        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
        Category category = categoryRepository.findById(categoryMetadataFieldValuesDto.getCategoryId()).get();
        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findById(categoryMetadataFieldValuesDto.getCategoryMetadataFieldId()).get();
        categoryMetadataFieldValues.setCategory(category);
        categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
        categoryMetadataFieldValues.setName(categoryMetadataFieldValuesDto.getName());
        CategoryMetadataFieldKey categoryMetadataFieldKey = new CategoryMetadataFieldKey(categoryMetadataFieldValuesDto.getCategoryMetadataFieldId(), categoryMetadataFieldValuesDto.getCategoryId());
        categoryMetadataFieldValues.setCategoryMetadataFieldKey(categoryMetadataFieldKey);
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
    }

    public void updateCategoryMetadataFieldValues(CategoryMetadataFieldValuesDto categoryMetadataFieldValuesDto)
    {
        CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findById(categoryMetadataFieldValuesDto.getCategoryMetadataFieldId(), categoryMetadataFieldValuesDto.getCategoryId());
        categoryMetadataFieldValues.setName(categoryMetadataFieldValuesDto.getName());
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
    }


    /* Product */

    public Product getProduct(Integer id)
    {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent())
        {
            throw new FieldNotFoundException("Product not found");
        }
        Product product1 = product.get();
        if(product1.isIs_Active() == false)
        {
            throw new ProductEntityException("Product is not been activated");
        }
        return product1;
    }

    public Iterable<Product> getAllProductDetails()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Product> products = productRepository.findAll(pageable);
        if(products.hasContent())
        {
            return products.getContent();
        }
        else
        {
            return new ArrayList<Product>();
        }
    }

    public ResponseEntity<String> activateProduct(Integer id)
    {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent())
        {
            Product product1 = product.get();
            if(!product1.isIs_Active())
            {
                product1.setIs_Active(true);
                productRepository.save(product1);
                return ResponseEntity.ok("Product Activated");
            }
            else
            {
                return ResponseEntity.ok("Product Already Activated");

            }
        }
        else
        {
            throw new FieldNotFoundException("Product not found");
        }
    }

    public ResponseEntity<String> deActivateProduct(Integer id)
    {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent())
        {
            Product product1 = product.get();
            if(product1.isIs_Active())
            {
                product1.setIs_Active(false);
                productRepository.save(product1);
                return ResponseEntity.ok("Product Deactivated");
            }
            else
            {
                return ResponseEntity.ok("Product Already Deactivated");

            }
        }
        else
        {
            throw new FieldNotFoundException("Product not found");
        }
    }

    //Order


    public List<Order> viewAllOrders()
    {
        Pageable pageable = PageRequest.of(0,10,Sort.by("id"));
        Page<Order> orders = orderRepository.findAll(pageable);
        if(orders.hasContent())
        {
            return orders.getContent();
        }
        else
        {
            return new ArrayList<Order>();
        }
    }

    public void changeOrderStatus(StatusDto statusDto) {
        Map<Enum, List<Enum>> enumList = orderStatusMap.setStatusEnums();
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(statusDto.getOrderProductId());
        if (orderProduct.isEmpty())
            throw new FieldNotFoundException("Invalid OrderProductId");

        if (statusDto.getFromStatus().name().equals(FromStatus.ORDER_PLACED.name()) && statusDto.getToStatus() == null)
            throw new FieldNotFoundException("ToStatus can't be null");

        OrderProduct orderProduct1 = orderProduct.get();
        OrderStatus orderStatus = orderProduct1.getOrderStatus();

        FromStatus fromStatusOld = FromStatus.valueOf(orderStatus.getFromStatus());


        if (statusDto.getFromStatus().name().equals(FromStatus.ORDER_PLACED.name())) {
            List<Enum> orderPlacedExpectedValues = enumList.get(statusDto.getFromStatus());
            if (orderPlacedExpectedValues.contains(statusDto.getToStatus())) {
                orderStatus.setFromStatus(statusDto.getFromStatus().name());
                orderStatus.setToStatus(statusDto.getToStatus().name());
                orderStatus.setTransitionNotesComments("From " + statusDto.getFromStatus().name() + " To " + statusDto.getToStatus().name());
                statusRepository.save(orderStatus);
                return;
            }
            throw new FieldNotFoundException(" ToStatus is not in range.");
        }

        List<Enum> toStatusList = enumList.get((fromStatusOld));
        if (toStatusList.contains(statusDto.getToStatus()) && orderStatus.getToStatus().equals(statusDto.getFromStatus().name())) {
            orderStatus.setFromStatus(statusDto.getFromStatus().name());
            orderStatus.setToStatus(statusDto.getToStatus().name());
            orderStatus.setTransitionNotesComments("From " + statusDto.getFromStatus().name() + " To " + statusDto.getToStatus().name());
            statusRepository.save(orderStatus);
            return;
        }
        throw new FieldNotFoundException("ToStatus is not in range");
    }


}


