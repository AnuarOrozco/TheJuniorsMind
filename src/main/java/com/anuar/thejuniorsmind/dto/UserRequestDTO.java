package com.anuar.thejuniorsmind.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank @Size(max = 50) String username,
        @Email @Size(max = 50) String email,
        @NotBlank @Size(min = 6) String password,
        String avatarUrl,
        String bio
) {}
