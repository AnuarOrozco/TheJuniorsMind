package com.anuar.thejuniorsmind.dto;

import java.time.LocalDateTime;

public record SubpostResponseDTO(
        Long id,
        String subtitle,
        String content,
        LocalDateTime createdAt,
        Long authorId,
        Long postId
) {}
