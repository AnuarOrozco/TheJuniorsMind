package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.config.TestConfig;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.User;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Technology")
                .iconUrl("https://example.com/icon.png")
                .build();

        // avoid the null xd
        if (category.getPosts() == null) {
            category.setPosts(new ArrayList<>());
        }

        category = categoryRepository.save(category);
    }

    @Test
    @DisplayName("Should save a category successfully")
    void testSaveCategory() {
        Category saved = categoryRepository.save(category);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Technology");
    }

    @Test
    @DisplayName("Should find category by ID")
    void testFindById() {
        Optional<Category> found = categoryRepository.findById(category.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Technology");
    }

    @Test
    @DisplayName("Should return all categories")
    void testFindAll() {
        Category another = Category.builder()
                .name("Science")
                .iconUrl("https://example.com/science.png")
                .build();

        categoryRepository.save(another);

        List<Category> all = categoryRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Should update category name")
    void testUpdateCategory() {
        category.setName("Tech & Innovation");
        Category updated = categoryRepository.save(category);
        assertThat(updated.getName()).isEqualTo("Tech & Innovation");
    }

    @Test
    @DisplayName("Should delete category")
    void testDeleteCategory() {
        categoryRepository.delete(category);
        Optional<Category> deleted = categoryRepository.findById(category.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should handle relationship with posts correctly")
    void testCategoryWithPosts() {
        // create user
        User author = User.builder()
                .username("anuar")
                .email("anuar@example.com")
                .password("password123")
                .build();
        entityManager.persist(author);

        // Create post
        Post post = Post.builder()
                .title("AI Revolution")
                .content("The impact of AI in modern society")
                .author(author)
                .category(category)
                .build();

        // Sinc bidirectional relationship
        category.getPosts().add(post);

        entityManager.persist(post);
        entityManager.flush();
        entityManager.clear(); // force reload in db

        Category found = categoryRepository.findById(category.getId()).orElseThrow();
        assertThat(found.getPosts()).hasSize(1);
        assertThat(found.getPosts().get(0).getTitle()).isEqualTo("AI Revolution");
    }
}