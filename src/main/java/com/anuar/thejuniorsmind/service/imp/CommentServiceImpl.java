package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.CommentRequestDTO;
import com.anuar.thejuniorsmind.dto.CommentResponseDTO;
import com.anuar.thejuniorsmind.exception.CommentNotFoundException;
import com.anuar.thejuniorsmind.mapper.CommentMapper;
import com.anuar.thejuniorsmind.model.Comment;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.CommentRepository;
import com.anuar.thejuniorsmind.repository.PostRepository;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO dto) {
        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.authorId()));

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + dto.postId()));

        Comment parent = null;
        if (dto.parentCommentId() != null) {
            parent = commentRepository.findById(dto.parentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + dto.parentCommentId()));
        }

        Comment comment = commentMapper.toEntity(dto, author, post, parent);
        Comment saved = commentRepository.save(comment);

        return commentMapper.toResponseDTO(saved);
    }

    @Override
    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return commentMapper.toResponseDTO(comment);
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return commentMapper.toResponseList(comments);
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.authorId()));

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + dto.postId()));

        Comment parent = null;
        if (dto.parentCommentId() != null) {
            parent = commentRepository.findById(dto.parentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + dto.parentCommentId()));
        }

        commentMapper.updateEntityFromDTO(comment, dto, author, post, parent);
        Comment updated = commentRepository.save(comment);

        return commentMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        if (comment.getReplies() != null) {
            comment.getReplies().forEach(reply -> reply.setParentComment(null));
        }

        commentRepository.delete(comment);
    }
}