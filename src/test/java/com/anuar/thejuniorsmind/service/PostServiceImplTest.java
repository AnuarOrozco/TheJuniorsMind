package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.PostRequestDTO;
import com.anuar.thejuniorsmind.dto.PostResponseDTO;
import com.anuar.thejuniorsmind.exception.*;
import com.anuar.thejuniorsmind.model.*;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.repository.*;
import com.anuar.thejuniorsmind.service.imp.PostServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User author;
    private Category category;
    private Tag tag;
    private Post post;
    private PostRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        author = User.builder().id(1L).username("John").build();
        category = Category.builder().id(1L).name("Tech").build();
        tag = Tag.builder().id(1L).name("Java").build();

        post = Post.builder()
                .id(1L)
                .title("My Post")
                .content("Content")
                .author(author)
                .category(category)
                .tags(List.of(tag))
                .build();

        requestDTO = new PostRequestDTO(
                "My Post",
                "Content",
                1L,
                1L,
                List.of(1L)
        );
    }

    // -------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should create post successfully")
    void testCreatePostSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(tagRepository.findAllById(List.of(1L))).thenReturn(List.of(tag));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostResponseDTO response = postService.createPost(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("My Post");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when author not found")
    void testCreatePost_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> postService.createPost(requestDTO));
    }

    // -------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should get post by id successfully")
    void testGetPostByIdSuccessfully() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponseDTO response = postService.getPostById(1L);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("My Post");
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw PostNotFoundException when post does not exist")
    void testGetPostById_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(1L));
    }

    // -------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should update post successfully")
    void testUpdatePostSuccessfully() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(tagRepository.findAllById(List.of(1L))).thenReturn(List.of(tag));
        when(postRepository.save(post)).thenReturn(post);

        PostResponseDTO response = postService.updatePost(1L, requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("My Post");
        verify(postRepository).save(post);
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should delete post successfully")
    void testDeletePostSuccessfully() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(postRepository).delete(post);
    }

    @Test
    @DisplayName("Should throw PostNotFoundException when deleting non-existent post")
    void testDeletePost_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.deletePost(1L));
    }

    // -------------------------------------------------------------
    // FILTERS (title, category, tag)
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should get posts by title successfully")
    void testGetPostsByTitleSuccessfully() {
        when(postRepository.findByTitleContainingIgnoreCase("My")).thenReturn(List.of(post));

        List<PostResponseDTO> responses = postService.getPostsByTitle("My");

        assertThat(responses).hasSize(1);
        verify(postRepository).findByTitleContainingIgnoreCase("My");
    }

    @Test
    @DisplayName("Should get posts by category successfully")
    void testGetPostsByCategorySuccessfully() {
        when(postRepository.findByCategoryId(1L)).thenReturn(List.of(post));

        List<PostResponseDTO> responses = postService.getPostsByCategory(1L);

        assertThat(responses).hasSize(1);
        verify(postRepository).findByCategoryId(1L);
    }

    @Test
    @DisplayName("Should get posts by tag successfully")
    void testGetPostsByTagSuccessfully() {
        when(postRepository.findByTagsName("Java")).thenReturn(List.of(post));

        List<PostResponseDTO> responses = postService.getPostsByTag("Java");

        assertThat(responses).hasSize(1);
        verify(postRepository).findByTagsName("Java");
    }
}
