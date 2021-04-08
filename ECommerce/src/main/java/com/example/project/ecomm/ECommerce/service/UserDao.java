package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.security.AppUser;
import com.example.project.ecomm.ECommerce.security.GrantAuthorityImpl;
import com.example.project.ecomm.ECommerce.entities.User.Role;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao
{
    private UserRepository userRepository;

    @Autowired
    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    AppUser loadUserByUsername(String email)
    {
        User user= userRepository.findByEmail(email); //because emails are unique
        if(email != null)
        {
            List<GrantAuthorityImpl> grantAuthorityList = new ArrayList<>();
            List<Role> roles = user.getRoleList();
            for(Role role : roles)
            {
                grantAuthorityList.add(new GrantAuthorityImpl(role.getAuthority()));
            }
            return  new AppUser(user.getEmail(), user.getPassword(), user.isIs_Active(), user.isAccountNotLocked(), grantAuthorityList);
        }
        else
        {
            throw new RuntimeException();
        }
    }
}
