package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.UserRequestDTO;
import com.anuar.thejuniorsmind.dto.UserResponseDTO;
import com.anuar.thejuniorsmind.exception.UserNotFoundException;
import com.anuar.thejuniorsmind.mapper.UserMapper;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.imp.UserServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequest;
    private UserResponseDTO userResponse;

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

        userResponse = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCreatedAt()
        );
    }

    @Test
    @DisplayName("Should create user successfully")
    void testCreateUserSuccessfully() {
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO response = userService.createUser(userRequest);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("john_doe");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserByIdSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO response = userService.getUserById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when ID does not exist")
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("Should get user by username successfully")
    void testGetUserByUsernameSuccessfully() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO response = userService.getUserByUsername("john_doe");

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("john_doe");
        verify(userRepository).findByUsername("john_doe");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when username not found")
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("john_doe"));
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserSuccessfully() {
        UserRequestDTO updateRequest = new UserRequestDTO("johnny", "johnny@example.com", "newpass", "avatar2.png", "New bio");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateEntityFromDTO(updateRequest, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        UserResponseDTO response = userService.updateUser(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(userMapper).updateEntityFromDTO(updateRequest, user);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when updating non-existent user")
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userRequest));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when deleting non-existent user")
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    @DisplayName("Should get all users successfully")
    void testGetAllUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(userResponse);

        List<UserResponseDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).username()).isEqualTo("john_doe");
        verify(userRepository).findAll();
    }
}