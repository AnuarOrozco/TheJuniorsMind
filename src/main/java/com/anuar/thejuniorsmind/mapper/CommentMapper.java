package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.CommentRequestDTO;
import com.anuar.thejuniorsmind.dto.CommentResponseDTO;
import com.anuar.thejuniorsmind.model.Comment;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    /**
     * Converts a CommentRequestDTO into a Comment entity.
     * Does not fetch related entities — they must be set in the service layer.
     */
    public Comment toEntity(CommentRequestDTO dto, User author, Post post, Comment parentComment) {
        return Comment.builder()
                .content(dto.content())
                .author(author)
                .post(post)
                .parentComment(parentComment)
                .build();
    }

    /**
     * Updates an existing Comment entity from a CommentRequestDTO.
     */
    public void updateEntityFromDTO(Comment comment, CommentRequestDTO dto, User author, Post post, Comment parentComment) {
        comment.setContent(dto.content());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setParentComment(parentComment);
    }

    /**
     * Converts a Comment entity into a CommentResponseDTO.
     */
    public CommentResponseDTO toResponseDTO(Comment comment) {
        List<Long> replyIds = comment.getReplies() != null
                ? comment.getReplies().stream().map(Comment::getId).collect(Collectors.toList())
                : List.of();

        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getAuthor().getId(),
                comment.getPost().getId(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                replyIds
        );
    }

    /**
     * Converts a list of Comment entities into a list of CommentResponseDTOs.
     */
    public List<CommentResponseDTO> toResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
