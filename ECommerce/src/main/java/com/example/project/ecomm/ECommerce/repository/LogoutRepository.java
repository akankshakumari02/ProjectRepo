package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.token.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRepository extends JpaRepository<LogoutToken,Long>
{
    LogoutToken findByToken(String token);
}
