package com.example.project.ecomm.ECommerce.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String address_not_found) {
        super(address_not_found);
    }
}
