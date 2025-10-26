package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.UserRequestDTO;
import com.anuar.thejuniorsmind.dto.UserResponseDTO;
import com.anuar.thejuniorsmind.exception.UserNotFoundException;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.imp.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequestDTO("john_doe", "john@example.com", "password123", "avatar.png", "Hello Bio");

        user = User.builder()
                .id(1L)
                .username(userRequest.username())
                .email(userRequest.email())
                .password(userRequest.password())
                .avatarUrl(userRequest.avatarUrl())
                .bio(userRequest.bio())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(userRequest.username());
        assertThat(response.email()).isEqualTo(userRequest.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByIdSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("john_doe");
    }

    @Test
    void testGetUserByIdThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUserByUsernameSuccessfully() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserByUsername("john_doe");

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("john_doe");
    }

    @Test
    void testGetUserByUsernameThrowsException() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("john_doe"));
    }

    @Test
    void testUpdateUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserRequestDTO updateRequest = new UserRequestDTO("johnny", "johnny@example.com", "newpass", "avatar2.png", "New bio");

        UserResponseDTO response = userService.updateUser(1L, updateRequest);

        assertThat(response.username()).isEqualTo("johnny");
        assertThat(response.email()).isEqualTo("johnny@example.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testGetAllUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).username()).isEqualTo("john_doe");
    }
}
