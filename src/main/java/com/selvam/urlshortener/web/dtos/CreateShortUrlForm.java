package com.selvam.urlshortener.web.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.Internal;

public record CreateShortUrlForm(
        @NotBlank(message = "Original URL is required")
        String originalUrl,
        Boolean isPrivate,
        @Min(1)
        @Max(365)
        Integer expirationInDays
) {
}
