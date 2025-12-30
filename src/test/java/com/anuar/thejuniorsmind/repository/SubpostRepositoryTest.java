package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.config.TestConfig;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Subpost;
import com.anuar.thejuniorsmind.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class SubpostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubpostRepository subpostRepository;

    private User author;
    private Category category;
    private Post post;
    private Subpost subpost;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .username("anuar")
                .email("anuar@example.com")
                .password("password123")
                .build();
        entityManager.persist(author);

        category = Category.builder()
                .name("Technology")
                .iconUrl("https://example.com/icon.png")
                .build();
        if (category.getPosts() == null) {
            category.setPosts(new java.util.ArrayList<>());
        }
        entityManager.persist(category);

        post = Post.builder()
                .title("AI Revolution")
                .content("The impact of AI in modern society")
                .author(author)
                .category(category)
                .build();
        if (post.getSubposts() == null) {
            post.setSubposts(new java.util.ArrayList<>());
        }
        entityManager.persist(post);

        subpost = Subpost.builder()
                .subtitle("Introduction to AI")
                .content("AI is transforming industries worldwide.")
                .author(author)
                .post(post)
                .build();
        entityManager.persist(subpost);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should save a subpost successfully")
    void testSaveSubpost() {
        Subpost newSubpost = Subpost.builder()
                .subtitle("Deep Learning Basics")
                .content("DL is a subset of AI.")
                .author(author)
                .post(post)
                .build();

        Subpost saved = subpostRepository.save(newSubpost);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSubtitle()).isEqualTo("Deep Learning Basics");
    }

    @Test
    @DisplayName("Should find subpost by ID")
    void testFindById() {
        Optional<Subpost> found = subpostRepository.findById(subpost.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getSubtitle()).isEqualTo("Introduction to AI");
    }

    @Test
    @DisplayName("Should return all subposts")
    void testFindAll() {
        Subpost another = Subpost.builder()
                .subtitle("Advanced AI")
                .content("AI is evolving rapidly.")
                .author(author)
                .post(post)
                .build();
        subpostRepository.save(another);

        List<Subpost> all = subpostRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Should update subpost content")
    void testUpdateSubpost() {
        subpost.setContent("Updated AI content.");
        Subpost updated = subpostRepository.save(subpost);
        assertThat(updated.getContent()).isEqualTo("Updated AI content.");
    }

    @Test
    @DisplayName("Should delete subpost")
    void testDeleteSubpost() {
        subpostRepository.delete(subpost);
        Optional<Subpost> deleted = subpostRepository.findById(subpost.getId());
        assertThat(deleted).isEmpty();
    }

}
