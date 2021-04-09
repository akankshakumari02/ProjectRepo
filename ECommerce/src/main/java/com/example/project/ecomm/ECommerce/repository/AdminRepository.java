package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<User,Integer> {
}
