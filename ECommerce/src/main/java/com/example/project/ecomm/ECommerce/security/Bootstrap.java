package com.example.project.ecomm.ECommerce.security;

import com.example.project.ecomm.ECommerce.entities.User.Role;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Bootstrap implements ApplicationRunner
{
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        if(userRepository.count() < 1)
        {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User admin = new User();
            admin.setFirstName("Akanksha");
            admin.setLastName("Kumari");
            admin.setEmail("ak02@gmail.com");
            admin.setPassword(passwordEncoder.encode("pass"));
            admin.setIs_Active(true);
            admin.setIs_Deleted(false);

            Role role1 = new Role();

            role1.setAuthority("ROLE_ADMIN");

            admin.setRoleList(Arrays.asList(role1));

            role1.setUserList(Arrays.asList(admin));

            userRepository.save(admin);
        }
    }
}
