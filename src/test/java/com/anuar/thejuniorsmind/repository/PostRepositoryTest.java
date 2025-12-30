package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.config.TestConfig;
import com.anuar.thejuniorsmind.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    private User author;
    private Category category;
    private Post post;
    private Tag tag;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .username("anuar_avalos")
                .email("anuar.dev@gmail.com")
                .password(("password123"))
                .build();

        category = Category.builder()
                .name("Spring Boot")
                .iconUrl("https://example.com/tech-icon.png")
                .build();

        tag = Tag.builder()
                .name("Spring Boot")
                .color(("#6DB33F"))
                .build();

        post = Post.builder()
                .title("Spring Boot Fundamentals")
                .content("Spring Boot simplifies application development in Java")
                .author(author)
                .category(category)
                .tags(new ArrayList<>()) // avoiding null
                .build();

        post.getTags().add(tag);
    }

    @Test
    @DisplayName("Should save a post successfully")
    void save_ValidPost_ReturnsSavedPost() {
        entityManager.persist(author);
        entityManager.persist(category);
        entityManager.persist(tag);

        Post savedPost = postRepository.save(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Spring Boot Fundamentals");
        assertThat(savedPost.getAuthor().getUsername()).isEqualTo("anuar_avalos");
    }

    @Test
    @DisplayName("Should find posts by title containing a keyword, ignoring case")
    void findByTitleContainingIgnoreCase_WhenExists_ReturnsMatchingPosts() {
        entityManager.persist(author);
        entityManager.persist(category);
        entityManager.persist(tag);
        entityManager.persistAndFlush(post);

        List<Post> foundPosts = postRepository.findByTitleContainingIgnoreCase("spring");

        assertThat(foundPosts).isNotEmpty();
        assertThat(foundPosts.get(0).getTitle()).containsIgnoringCase("spring");
    }

    @Test
    @DisplayName("Should find posts by category id")
    void findByCategoryId_WhenExists_ReturnsMatchingPosts() {
        entityManager.persist(author);
        Category savedCategory = entityManager.persistAndFlush(category);
        entityManager.persist(tag);
        entityManager.persistAndFlush(post);

        List<Post> foundPosts = postRepository.findByCategoryId(savedCategory.getId());

        assertThat(foundPosts).isNotEmpty();
        assertThat(foundPosts.get(0).getCategory().getId()).isEqualTo(savedCategory.getId());
    }

    @Test
    @DisplayName("Should find posts by tag name")
    void findByTagsName_WhenExists_ReturnsMatchingPosts() {
        entityManager.persist(author);
        entityManager.persist(category);
        entityManager.persist(tag);
        entityManager.persistAndFlush(post);

        List<Post> foundPosts = postRepository.findByTagsName("Spring Boot");

        assertThat(foundPosts).isNotEmpty();
        assertThat(foundPosts.get(0).getTags()).extracting(Tag::getName).contains("Spring Boot");
    }

    @Test
    @DisplayName("Should return empty list when no post matches the title")
    void findByTitleContainingIgnoreCase_WhenNoMatch_ReturnsEmpty() {
        List<Post> foundPosts = postRepository.findByTitleContainingIgnoreCase("Python");

        assertThat(foundPosts).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when category has no posts")
    void findByCategoryId_WhenNoPosts_ReturnsEmpty() {
        List<Post> foundPosts = postRepository.findByCategoryId(999L);

        assertThat(foundPosts).isEmpty();
    }
}
