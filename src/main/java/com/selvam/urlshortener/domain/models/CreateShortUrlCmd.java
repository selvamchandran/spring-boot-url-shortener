package com.selvam.urlshortener.domain.models;

public record CreateShortUrlCmd(String originalUrl,
                                Boolean isPrivate,
                                Integer expirationInDays,
                                Long userId) {
}
