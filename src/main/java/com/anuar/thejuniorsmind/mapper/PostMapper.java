package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.PostRequestDTO;
import com.anuar.thejuniorsmind.dto.PostResponseDTO;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    /**
     * Convierte un DTO de solicitud (PostRequestDTO) a una entidad Post.
     *
     * @param dto      DTO con los datos del post.
     * @param author   Entidad User asociada (ya validada).
     * @param category Entidad Category asociada (ya validada).
     * @param tags     Lista de entidades Tag asociadas.
     * @return Entidad Post lista para persistir.
     */
    public Post toEntity(PostRequestDTO dto, User author, Category category, List<Tag> tags) {
        if (dto == null) return null;

        return Post.builder()
                .title(dto.title())
                .content(dto.content())
                .author(author)
                .category(category)
                .tags(tags)
                .build();
    }

    /**
     * Convierte una entidad Post a su DTO de respuesta (PostResponseDTO).
     *
     * @param post Entidad Post.
     * @return DTO con los datos del post.
     */
    public PostResponseDTO toResponseDTO(Post post) {
        if (post == null) return null;

        List<String> tagNames = post.getTags() != null
                ? post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList())
                : List.of();

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getAuthor() != null ? post.getAuthor().getId() : null,
                post.getCategory() != null ? post.getCategory().getId() : null,
                tagNames
        );
    }
}