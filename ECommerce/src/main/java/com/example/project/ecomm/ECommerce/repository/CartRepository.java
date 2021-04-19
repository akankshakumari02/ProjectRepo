package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import com.example.project.ecomm.ECommerce.entities.Order.CustomerProductVariationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, CustomerProductVariationKey>
{
    Optional<Cart> findByProductVariationId(int id);

    @Query("from Cart where customer_id = ?1")
    List<Cart> findById(int customerId);

    @Modifying
    @Query("delete from Cart where customer_id = ?1")
    List<Cart> deleteAllProduct(int customerId);

    @Modifying
    @Query("delete from Cart where customer_id = ?1 and product_variation_id = ?2")
    void deleteProduct(int customerId, int productVariationId);
}
