package com.example.project.ecomm.ECommerce.repository;

import com.example.project.ecomm.ECommerce.entities.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    @Query("from User u where u.email=:email and u.password=:password")
    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    User findByEmail(String email);

    @Query("Update User u set u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
   public void updateAttempts(int failedAttempts, String email);
}
