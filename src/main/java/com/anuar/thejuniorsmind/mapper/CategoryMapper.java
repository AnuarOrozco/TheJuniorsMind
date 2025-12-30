package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.CategoryRequestDTO;
import com.anuar.thejuniorsmind.dto.CategoryResponseDTO;
import com.anuar.thejuniorsmind.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    // DTO de request → Entidad
    public Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Category.builder()
                .name(dto.name())
                .iconUrl(dto.iconUrl())
                .build();
    }

    // Entidad → DTO de respuesta
    public CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }

        List<Long> postIds = category.getPosts() != null
                ? category.getPosts().stream()
                .map(post -> post.getId())
                .collect(Collectors.toList())
                : List.of();

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getIconUrl(),
                postIds
        );
    }

    // lista de entidades → lista de DTOs
    public List<CategoryResponseDTO> toResponseList(List<Category> categories) {
        if (categories == null) {
            return List.of();
        }

        return categories.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualiza entidad existente con datos del DTO
    public void updateEntityFromDTO(CategoryRequestDTO dto, Category category) {
        if (dto == null || category == null) {
            return;
        }

        category.setName(dto.name());
        category.setIconUrl(dto.iconUrl());
    }
}
