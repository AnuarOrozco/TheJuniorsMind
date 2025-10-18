package com.anuar.thejuniorsmind.dto;

import java.util.List;

public record CategoryResponseDTO(
        Long id,
        String name,
        String iconUrl,
        List<Long> postIds
) {}
