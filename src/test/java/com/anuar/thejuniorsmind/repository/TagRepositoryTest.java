package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.config.TestConfig;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Tag;
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
public class TagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    private User author;
    private Category category;
    private Post post;
    private Tag tag;

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
        if (post.getTags() == null) {
            post.setTags(new java.util.ArrayList<>());
        }
        entityManager.persist(post);

        tag = Tag.builder()
                .name("AI")
                .color("#FF0000")
                .build();
        if (tag.getPosts() == null) {
            tag.setPosts(new java.util.ArrayList<>());
        }
        entityManager.persist(tag);

        // Associate tag with post
        post.getTags().add(tag);
        tag.getPosts().add(post);

        entityManager.persist(post);
        entityManager.persist(tag);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should save a tag successfully")
    void testSaveTag() {
        Tag newTag = Tag.builder()
                .name("Machine Learning")
                .color("#00FF00")
                .build();
        Tag saved = tagRepository.save(newTag);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Machine Learning");
    }

    @Test
    @DisplayName("Should find tag by ID")
    void testFindById() {
        Optional<Tag> found = tagRepository.findById(tag.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("AI");
    }

    @Test
    @DisplayName("Should return all tags")
    void testFindAll() {
        Tag another = Tag.builder()
                .name("Deep Learning")
                .color("#0000FF")
                .build();
        tagRepository.save(another);

        List<Tag> all = tagRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Should update tag name and color")
    void testUpdateTag() {
        tag.setName("Artificial Intelligence");
        tag.setColor("#123456");

        Tag updated = tagRepository.save(tag);

        assertThat(updated.getName()).isEqualTo("Artificial Intelligence");
        assertThat(updated.getColor()).isEqualTo("#123456");
    }

    @Test
    @DisplayName("Should delete tag")
    void testDeleteTag() {
        tagRepository.delete(tag);

        Optional<Tag> deleted = tagRepository.findById(tag.getId());

        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should handle relationship with posts correctly")
    void testTagWithPosts() {
        Tag found = tagRepository.findById(tag.getId()).orElseThrow();

        assertThat(found.getPosts()).hasSize(1);
        assertThat(found.getPosts().get(0).getTitle()).isEqualTo("AI Revolution");
    }

}
