package com.example.project.ecomm.ECommerce.exception;

@SuppressWarnings("serial")
public class EmailAlreadyExistsException extends RuntimeException
{
    public EmailAlreadyExistsException(final String message)
    {
        super(message);
    }
}
