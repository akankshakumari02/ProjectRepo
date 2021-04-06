package com.example.project.ecomm.ECommerce.exception;

public class InvalidTokenException extends RuntimeException
{
    public InvalidTokenException(final String message) {
        super(message);
    }
}
