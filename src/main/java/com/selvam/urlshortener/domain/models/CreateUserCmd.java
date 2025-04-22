package com.selvam.urlshortener.domain.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateUserCmd(String email,
                            String password,
                            String name,
                            Role role
) {
}
