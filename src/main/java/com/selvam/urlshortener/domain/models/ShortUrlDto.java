package com.selvam.urlshortener.domain.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

public record ShortUrlDto(Long id, String shortKey, String originalUrl,
                          UserDto createdBy, Boolean isPrivate, Long clickCount,
                          LocalDateTime createdAt, LocalDateTime expiresAt
                          ) implements Serializable {
}
