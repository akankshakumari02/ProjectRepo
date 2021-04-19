package com.example.project.ecomm.ECommerce.repository;


import com.example.project.ecomm.ECommerce.entities.Product.CategoryMetadataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField, Integer>
{
    CategoryMetadataField findByName(String name);
    Page<CategoryMetadataField> findAll(Pageable pageable);
}
