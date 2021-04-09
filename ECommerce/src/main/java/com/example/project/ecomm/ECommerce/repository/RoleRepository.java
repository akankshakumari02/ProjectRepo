package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.User.Role;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByAuthority(String authority);
}
