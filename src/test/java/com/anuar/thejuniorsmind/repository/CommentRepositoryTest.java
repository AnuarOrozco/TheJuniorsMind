package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.config.TestConfig;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Comment;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private User author;
    private Category category;
    private Post post;
    private Comment comment;

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
        if (post.getComments() == null) {
            post.setComments(new java.util.ArrayList<>());
        }
        entityManager.persist(post);

        // Create main comment
        comment = Comment.builder()
                .content("Great article!")
                .author(author)
                .post(post)
                .build();
        if (comment.getReplies() == null) {
            comment.setReplies(new java.util.ArrayList<>());
        }
        entityManager.persist(comment);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should save a comment successfully")
    void testSaveComment() {
        Comment newComment = Comment.builder()
                .content("Another comment")
                .author(author)
                .post(post)
                .build();
        Comment saved = commentRepository.save(newComment);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("Another comment");
    }

    @Test
    @DisplayName("Should find comment by ID")
    void testFindById() {
        Optional<Comment> found = commentRepository.findById(comment.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Great article!");
    }

    @Test
    @DisplayName("Should return all comments")
    void testFindAll() {
        Comment another = Comment.builder()
                .content("Another comment")
                .author(author)
                .post(post)
                .build();
        commentRepository.save(another);

        List<Comment> all = commentRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Should update comment content")
    void testUpdateComment() {
        comment.setContent("Updated content");
        Comment updated = commentRepository.save(comment);
        assertThat(updated.getContent()).isEqualTo("Updated content");
    }

    @Test
    @DisplayName("Should delete comment")
    void testDeleteComment() {
        commentRepository.delete(comment);
        Optional<Comment> deleted = commentRepository.findById(comment.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should handle replies (child comments) correctly")
    void testCommentReplies() {
        Comment reply = Comment.builder()
                .content("I agree!")
                .author(author)
                .post(post)
                .parentComment(comment)
                .build();
        if (comment.getReplies() == null) {
            comment.setReplies(new java.util.ArrayList<>());
        }
        comment.getReplies().add(reply);

        entityManager.persist(reply);
        entityManager.flush();
        entityManager.clear();

        Comment found = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(found.getReplies()).hasSize(1);
        assertThat(found.getReplies().get(0).getContent()).isEqualTo("I agree!");
    }

}
