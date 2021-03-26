package com.example.project.ecomm.ECommerce.controller;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.service.CustomerService;
import com.example.project.ecomm.ECommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/seller")
public class SellerController
{
    private SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService)
    {
        this.sellerService = sellerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerSeller(@Valid @RequestBody Seller seller, final HttpServletRequest request) {
        sellerService.register(seller);
        return new ResponseEntity<>("Account successfully create", HttpStatus.OK);
    }
    /*@Autowired
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
    }*/
}
