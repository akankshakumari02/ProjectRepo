package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    User findByEmail(String email);
}
