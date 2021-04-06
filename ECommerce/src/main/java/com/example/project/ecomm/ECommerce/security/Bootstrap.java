package com.example.project.ecomm.ECommerce.security;

import com.example.project.ecomm.ECommerce.entities.User.Role;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.RoleRepository;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class Bootstrap implements ApplicationRunner
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception
    {
        if(userRepository.count() < 1)
        {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User admin = new User();
            admin.setFirstName("Akanksha");
            admin.setMiddleName("Kumari");
            admin.setLastName("Pathania");
            admin.setEmail("aakanksha02@gmail.com");
            admin.setPassword(passwordEncoder.encode("Ak021997*"));
            admin.setIs_Active(true);
            admin.setIs_Deleted(false);

            Role role_admin = new Role();
            role_admin.setAuthority("ROLE_ADMIN");

            Role role_seller = new Role();
            role_seller.setAuthority("ROLE_SELLER");

            Role role_customer = new Role();
            role_customer.setAuthority("ROLE_CUSTOMER");

            admin.setRoleList(Arrays.asList(role_admin));

            role_admin.setUserList(Arrays.asList(admin));

            roleRepository.saveAll(List.of(role_customer,role_seller,role_admin));

            userRepository.save(admin);

        }
    }
}
