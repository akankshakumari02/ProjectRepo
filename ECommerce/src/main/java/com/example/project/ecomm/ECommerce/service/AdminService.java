 package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.Product.*;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.exception.CategoryAlreadyExistsException;
import com.example.project.ecomm.ECommerce.exception.UserNotFoundException;
import com.example.project.ecomm.ECommerce.repository.*;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldDto;
import com.example.project.ecomm.ECommerce.validatorobjects.CategoryMetadataFieldValuesDto;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

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

    public Iterable<Customer> getCustomer()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Customer> allCustomers = customerRepository.findAll(pageable);
        return allCustomers;
    }

    public Iterable<Seller> getSeller()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Seller> allSellers = sellerRepository.findAll(pageable);
        return allSellers;
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
                return ResponseEntity.ok("Account already active");
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
        Category category1 = category.get();
        return category1;
    }

    public Iterable<Category> getAllCategories()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage;
    }

    public void updateCategory(CategoryDto categoryDto)
    {
        Optional<Category> category = categoryRepository.findById(categoryDto.getId());
        category.orElseThrow(() -> new RuntimeException("Category not Found"));
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
            throw new RuntimeException("CategoryMetadataField Already exists");

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
            throw new RuntimeException("CategoryMetadata Field Values With Same Info Already Exists");
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
        categoryMetadataFieldValues.setName(categoryMetadataFieldValues.getName());
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
    }


    /* Product */

    public Product getProduct(Integer id)
    {
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent())
        {
            throw new RuntimeException("Product not found");
        }
        Product product1 = product.get();
        if(product1.isIs_Active() == false)
        {
            throw new RuntimeException("Product is not been activated");
        }
        return product1;
    }

    public Iterable<Product> getAllProductDetails()
    {
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));
        Page<Product> products = productRepository.findAll(pageable);
        return products;
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
            throw new RuntimeException("Product not found");
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
            throw new RuntimeException("Product not found");
        }
    }
}
