package com.anuar.thejuniorsmind.dto;

import java.util.List;

public record TagResponseDTO(
        Long id,
        String name,
        String color,
        List<Long> postIds
) {}
