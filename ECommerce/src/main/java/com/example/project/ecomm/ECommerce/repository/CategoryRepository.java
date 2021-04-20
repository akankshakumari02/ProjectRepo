package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Product.Category;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>
{
    Optional<Category> findById(Integer id);

    Category findByName(String name);

    Page<Category> findAll(Pageable pageable);
}
