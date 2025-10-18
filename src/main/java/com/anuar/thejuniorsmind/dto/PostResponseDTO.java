package com.anuar.thejuniorsmind.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDTO(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long authorId,
        Long categoryId,
        List<String> tags
) {}
