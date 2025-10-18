package com.anuar.thejuniorsmind.dto;

public record SubpostRequestDTO(
        String subtitle,
        String content,
        Long authorId,
        Long postId
) {}
