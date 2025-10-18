package com.anuar.thejuniorsmind.dto;

public record CommentRequestDTO(
        String content,
        Long authorId,
        Long postId,
        Long parentCommentId
) {}
