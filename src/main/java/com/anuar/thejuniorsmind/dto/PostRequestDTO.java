package com.anuar.thejuniorsmind.dto;

import java.util.List;

public record PostRequestDTO(
        String title,
        String content,
        Long authorId,
        Long categoryId,
        List<Long> tagIds
) {}
