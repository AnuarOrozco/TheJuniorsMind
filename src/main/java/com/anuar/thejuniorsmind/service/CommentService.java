package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.CommentRequestDTO;
import com.anuar.thejuniorsmind.dto.CommentResponseDTO;

import java.util.List;

public interface CommentService {


    CommentResponseDTO createComment(CommentRequestDTO commentRequest);
    CommentResponseDTO getCommentById(Long id);
    List<CommentResponseDTO> getAllComments();
    CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequest);
    void deleteComment(Long id);

}
