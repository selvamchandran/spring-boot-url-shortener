package com.selvam.urlshortener.domain.exceptions;

public class ShortUrlNotFoundException extends RuntimeException{
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
