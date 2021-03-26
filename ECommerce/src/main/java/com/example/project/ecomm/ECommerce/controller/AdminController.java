package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class AdminController
{
    @Autowired
    AdminService adminService;

    @PostMapping("/addAdmin")
    public ResponseEntity<User> addAdmin(@RequestBody User admin)
    {
        User admin1 = adminService.addAdmin(admin);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(admin1.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/getAdmin/{id}")
    public User getAdmin(@PathVariable int id) throws Exception
    {
        return adminService.getAdmin(id);
    }

    @GetMapping("/getAllAdmins")
    public List<User> getAllAdmins() throws Exception
    {
        return adminService.getAllAdmins();
    }
}
