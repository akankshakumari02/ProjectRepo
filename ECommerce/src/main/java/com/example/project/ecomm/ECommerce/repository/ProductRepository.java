package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>
{
    Product findByNameAndId(String name, Integer categoryId);
    Page<Product> findAll(Pageable pageable);
}
