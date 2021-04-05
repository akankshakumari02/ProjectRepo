package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>
{

    //values displayed by admin
    Page<Customer> findAll(Pageable pageable);

    Customer findByEmail(String email);

}
