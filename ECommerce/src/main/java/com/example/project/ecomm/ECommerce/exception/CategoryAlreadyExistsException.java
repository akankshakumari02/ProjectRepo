package com.example.project.ecomm.ECommerce.exception;

public class CategoryAlreadyExistsException extends RuntimeException
{
    public CategoryAlreadyExistsException(String message)
    {
        super(message);
    }
}
