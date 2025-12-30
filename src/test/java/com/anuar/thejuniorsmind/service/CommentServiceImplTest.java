package com.anuar.thejuniorsmind.service;

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
import com.anuar.thejuniorsmind.service.imp.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Post post;
    private Comment comment;
    private CommentRequestDTO request;
    private CommentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("Anuar")
                .build();

        post = Post.builder()
                .id(1L)
                .title("Test Post")
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("Great post!")
                .author(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        request = new CommentRequestDTO("Great post!", 1L, 1L, null);

        responseDTO = new CommentResponseDTO(
                1L,
                "Great post!",
                comment.getCreatedAt(),
                1L,
                1L,
                null,
                List.of()
        );
    }

    @Test
    @DisplayName("Should create a comment successfully")
    void testCreateCommentSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentMapper.toEntity(request, user, post, null)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO response = commentService.createComment(request);

        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo("Great post!");
        assertThat(response.authorId()).isEqualTo(1L);
        assertThat(response.postId()).isEqualTo(1L);

        verify(commentMapper).toEntity(request, user, post, null);
        verify(commentRepository).save(comment);
        verify(commentMapper).toResponseDTO(comment);
    }

    @Test
    @DisplayName("Should throw exception when post not found on create")
    void testCreateComment_PostNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found");

        verify(commentRepository, never()).save(any());
        verify(commentMapper, never()).toEntity(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should get a comment by ID successfully")
    void testGetCommentByIdSuccessfully() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO response = commentService.getCommentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.content()).isEqualTo("Great post!");
        assertThat(response.authorId()).isEqualTo(1L);

        verify(commentMapper).toResponseDTO(comment);
    }

    @Test
    @DisplayName("Should throw exception when comment not found by ID")
    void testGetCommentById_NotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getCommentById(1L))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("Comment not found");

        verify(commentRepository).findById(1L);
        verify(commentMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Should update comment content successfully")
    void testUpdateCommentSuccessfully() {
        CommentRequestDTO updateRequest = new CommentRequestDTO("Updated content", 1L, 1L, null);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO response = commentService.updateComment(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(commentMapper).updateEntityFromDTO(comment, updateRequest, user, post, null);
        verify(commentRepository).save(comment);
    }

    @Test
    @DisplayName("Should delete a comment and its replies successfully")
    void testDeleteCommentSuccessfully() {
        Comment reply = Comment.builder()
                .id(2L)
                .content("Reply")
                .author(user)
                .post(post)
                .parentComment(comment)
                .build();

        comment.setReplies(List.of(reply));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(comment);
        assertThat(reply.getParentComment()).isNull();
    }
}