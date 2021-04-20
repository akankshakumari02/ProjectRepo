package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.Order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>
{
    Page<Order> findByCustomerId(int customerId, Pageable pageable);
}
