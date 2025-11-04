package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.SubpostRequestDTO;
import com.anuar.thejuniorsmind.dto.SubpostResponseDTO;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Subpost;
import com.anuar.thejuniorsmind.model.User;
import org.springframework.stereotype.Component;

@Component
public class SubpostMapper {

    /**
     * Convierte un DTO de solicitud en una entidad Subpost.
     * Las relaciones con User y Post se inyectan desde el servicio.
     */
    public Subpost toEntity(SubpostRequestDTO dto, User author, Post post) {
        if (dto == null) return null;

        return Subpost.builder()
                .subtitle(dto.subtitle())
                .content(dto.content())
                .author(author)
                .post(post)
                .build();
    }

    /**
     * Actualiza una entidad Subpost existente con los datos del DTO.
     * No reemplaza autor o post a menos que se actualicen explícitamente en el servicio.
     */
    public void updateEntity(Subpost subpost, SubpostRequestDTO dto, User author, Post post) {
        if (dto == null) return;

        subpost.setSubtitle(dto.subtitle());
        subpost.setContent(dto.content());

        if (author != null) {
            subpost.setAuthor(author);
        }
        if (post != null) {
            subpost.setPost(post);
        }
    }

    /**
     * Convierte una entidad Subpost en un DTO de respuesta.
     */
    public SubpostResponseDTO toResponseDTO(Subpost subpost) {
        if (subpost == null) return null;

        return new SubpostResponseDTO(
                subpost.getId(),
                subpost.getSubtitle(),
                subpost.getContent(),
                subpost.getCreatedAt(),
                subpost.getAuthor() != null ? subpost.getAuthor().getId() : null,
                subpost.getPost() != null ? subpost.getPost().getId() : null
        );
    }
}
