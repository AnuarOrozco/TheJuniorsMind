package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.CommentRequestDTO;
import com.anuar.thejuniorsmind.dto.CommentResponseDTO;
import com.anuar.thejuniorsmind.exception.CommentNotFoundException;
import com.anuar.thejuniorsmind.model.Comment;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.CommentRepository;
import com.anuar.thejuniorsmind.repository.PostRepository;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.content());

        User author = userRepository.findById(commentRequest.authorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentRequest.authorId()));
        comment.setAuthor(author);

        Post post = postRepository.findById(commentRequest.postId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentRequest.postId()));
        comment.setPost(post);

        if (commentRequest.parentCommentId() != null) {
            Comment parent = commentRepository.findById(commentRequest.parentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + commentRequest.parentCommentId()));
            comment.setParentComment(parent);
        }

        Comment saved = commentRepository.save(comment);
        return mapToResponseDTO(saved);
    }

    @Override
    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return mapToResponseDTO(comment);
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        comment.setContent(commentRequest.content());

        if (commentRequest.authorId() != null && !comment.getAuthor().getId().equals(commentRequest.authorId())) {
            User author = userRepository.findById(commentRequest.authorId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + commentRequest.authorId()));
            comment.setAuthor(author);
        }

        if (commentRequest.postId() != null && !comment.getPost().getId().equals(commentRequest.postId())) {
            Post post = postRepository.findById(commentRequest.postId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentRequest.postId()));
            comment.setPost(post);
        }

        if (commentRequest.parentCommentId() != null) {
            Comment parent = commentRepository.findById(commentRequest.parentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with id: " + commentRequest.parentCommentId()));
            comment.setParentComment(parent);
        } else {
            comment.setParentComment(null);
        }

        Comment updated = commentRepository.save(comment);
        return mapToResponseDTO(updated);
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

    private CommentResponseDTO mapToResponseDTO(Comment comment) {
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
}
