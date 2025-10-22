package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Should find an user by username when the user exists")
    void findByUsername_WhenUserExists_ReturnsUser() {
        // Arrange
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return empty when finding and user by a non-existent username")
    void findByUsername_WhenUserNotExists_ReturnsEmpty() {
        Optional<User> foundUser = userRepository.findByUsername("osomunoz");

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should find an user by its email when user exists")
    void findByEmail_WhenUserExists_ReturnsUser() {
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should return empty when finding and user by a non-existent email")
    void findByEmail_WhenUserNotExists_ReturnsEmpty() {
        Optional<User> foundUser = userRepository.findByEmail("osomunoz@gmail.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should save the user successfully")
    void save_ValidUser_ReturnsSavedUser() {
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should find an user by its id when the user exists")
    void findById_WhenUserExists_ReturnsUser() {
        User savedUser = entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

}
