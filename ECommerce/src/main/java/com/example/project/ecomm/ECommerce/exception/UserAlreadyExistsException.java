package com.example.project.ecomm.ECommerce.exception;

public class UserAlreadyExistsException extends RuntimeException
{
    private UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
