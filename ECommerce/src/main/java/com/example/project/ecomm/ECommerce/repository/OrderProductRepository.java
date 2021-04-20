package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct,Integer>
{
    List<OrderProduct> findByProductVariationId(int id);
}
