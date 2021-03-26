package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService
{
    @Autowired
    AdminRepository userRepository;


    public User addAdmin(User user){

        return userRepository.save(user);

    }

    public User getAdmin(int id) throws Exception {

        Optional<User> optionalUser;

        optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent())
        {
            return optionalUser.get();
        }
        else
        {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public User updateAdmin(User admin){

        return userRepository.save(admin);
    }

    public User removeAdmin(int id){

        User admin=userRepository.findById(id).get();
        userRepository.delete(admin);

        return admin;

    }

    public List<User> getAllAdmins() throws Exception {

        return (List<User>) userRepository.findAll();
    }
}
