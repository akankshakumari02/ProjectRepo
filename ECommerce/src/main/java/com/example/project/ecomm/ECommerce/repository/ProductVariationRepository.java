package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariationRepository extends JpaRepository<ProductVariation,Integer>
{
    Optional<ProductVariation> findById(Long id);
}
