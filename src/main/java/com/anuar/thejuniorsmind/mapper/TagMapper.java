package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;
import com.anuar.thejuniorsmind.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    /**
     * Convierte un TagRequestDTO a entidad Tag.
     */
    public Tag toEntity(TagRequestDTO dto) {
        if (dto == null) return null;

        return Tag.builder()
                .name(dto.name())
                .color(dto.color())
                .build();
    }

    /**
     * Convierte una entidad Tag a TagResponseDTO.
     */
    public TagResponseDTO toResponseDTO(Tag tag) {
        if (tag == null) return null;

        List<Long> postIds = tag.getPosts() != null
                ? tag.getPosts().stream().map(post -> post.getId()).collect(Collectors.toList())
                : List.of();

        return new TagResponseDTO(
                tag.getId(),
                tag.getName(),
                tag.getColor(),
                postIds
        );
    }

    /**
     * Actualiza los campos editables de una entidad Tag a partir de un TagRequestDTO.
     */
    public void updateEntityFromDTO(TagRequestDTO dto, Tag tag) {
        if (dto == null || tag == null) return;

        tag.setName(dto.name());
        tag.setColor(dto.color());
    }
}