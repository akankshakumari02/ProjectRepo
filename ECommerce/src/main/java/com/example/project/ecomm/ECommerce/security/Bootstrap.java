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

            Role role1 = new Role();
            role1.setAuthority("ROLE_ADMIN");

            Role role2 = new Role();
            role2.setAuthority("ROLE_CUSTOMER");

            Role role3 = new Role();
            role3.setAuthority("ROLE_SELLER");

            admin.setRoleList(Arrays.asList(role1));

            role1.setUserList(Arrays.asList(admin));

            roleRepository.saveAll(List.of(role3, role2, role1));

            userRepository.save(admin);
        }
    }
}
