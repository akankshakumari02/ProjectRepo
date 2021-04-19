
package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<ConfirmationToken, Integer>
{

    ConfirmationToken findByToken(String confirmationToken);

    ConfirmationToken findTokenByUserId(int id);
}

