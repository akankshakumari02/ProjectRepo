package com.example.project.ecomm.ECommerce.exception;

public class CartEmptyException extends RuntimeException
{
    public CartEmptyException(final String message)
    {
        super(message);
    }
}
