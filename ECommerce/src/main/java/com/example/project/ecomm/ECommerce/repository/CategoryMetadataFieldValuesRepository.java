package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Product.CategoryMetadataFieldKey;
import com.example.project.ecomm.ECommerce.entities.Product.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, CategoryMetadataFieldKey> {

    @Query("from CategoryMetadataFieldValues  where category_metadata_field_id = ?1 and category_id = ?2")
    CategoryMetadataFieldValues findById(Integer categoryMetadataFieldId,Integer categoryId);
}