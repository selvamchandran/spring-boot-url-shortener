package com.selvam.urlshortener.web;

import com.selvam.urlshortener.domain.exceptions.ShortUrlNotFoundException;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public String handleShortUrlNotFoundException(ShortUrlNotFoundException ex){
        log.error("Short URL not found : {}", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex){
        log.error("Unhandled Exception : {}", ex.getMessage());
        return "error/500";
    }
}
