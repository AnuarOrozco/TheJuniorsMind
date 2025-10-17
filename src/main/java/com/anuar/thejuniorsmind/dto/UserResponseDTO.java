package com.anuar.thejuniorsmind.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String avatarUrl,
        String bio,
        LocalDateTime createdAt
) {}
