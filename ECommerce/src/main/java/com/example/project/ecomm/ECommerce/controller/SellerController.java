package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SellerController
{
    @Autowired
    SellerService sellerService;

    @PostMapping("/addSeller")
    public ResponseEntity<Seller> addSeller(@RequestBody Seller seller)
    {
        Seller seller1 = sellerService.addSeller(seller);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(seller1.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/getSeller/{id}")
    public Seller getSeller(@PathVariable int id) throws Exception {

        return sellerService.getSeller(id);

    }

    @GetMapping("/getAllSellers")
    public List<Seller> getAllSellers() throws Exception {

        return sellerService.getAllSellers();
    }
}
