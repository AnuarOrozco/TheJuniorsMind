package com.anuar.thejuniorsmind.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponseDTO(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long authorId,
        Long postId,
        Long parentCommentId,
        List<Long> replyIds
) {}
