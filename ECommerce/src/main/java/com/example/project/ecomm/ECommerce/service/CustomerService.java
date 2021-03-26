package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.Address;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService
{
    @Autowired
    CustomerRepository customerRepository;

    public Customer addCustomer(Customer customer)
    {
        List<Address> addressList = customer.getAddressList();

        for(Address address : addressList)
        {
            address.setCustomer(customer);
        }

        return customerRepository.save(customer);
    }

    public Customer getCustomer(int id) throws Exception
    {
        Optional<Customer> optionalCustomer;
        optionalCustomer = customerRepository.findById(id);

        if(optionalCustomer.isPresent())
        {
            return optionalCustomer.get();
        }
        else
        {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public Customer updateCustomer(Customer customer)
    {
        return customerRepository.save(customer);
    }

    public User removeCustomer(int id)
    {
        Customer customer = customerRepository.findById(id).get();
        customerRepository.delete(customer);

        return customer;
    }

    public List<Customer> getAllCustomers() throws Exception
    {
        return (List<Customer>) customerRepository.findAll();
    }
}
