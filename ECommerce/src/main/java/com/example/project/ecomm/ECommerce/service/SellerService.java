package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService
{
    @Autowired
    SellerRepository sellerRepository;

    public Seller addSeller(Seller seller)
    {
        Address address = seller.getAddress();
        address.setSeller(seller);
        return sellerRepository.save(seller);
    }

    public Seller getSeller(int id) throws Exception
    {
        Optional<Seller> optionalSeller;
        optionalSeller = sellerRepository.findById(id);
        if(optionalSeller.isPresent())
        {
            return optionalSeller.get();
        }
        else
        {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public Seller updateSeller(Seller seller)
    {
        return sellerRepository.save(seller);
    }

    public Seller removeSeller(int id)
    {
        Seller seller = sellerRepository.findById(id).get();
        sellerRepository.delete(seller);

        return seller;
    }

    public List<Seller> getAllSellers() throws Exception
    {
        return (List<Seller>) sellerRepository.findAll();
    }
}
