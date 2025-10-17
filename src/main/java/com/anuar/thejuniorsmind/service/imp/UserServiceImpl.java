package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.UserRequestDTO;
import com.anuar.thejuniorsmind.dto.UserResponseDTO;
import com.anuar.thejuniorsmind.exception.UserNotFoundException;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(userRequest.password())
                .avatarUrl(userRequest.avatarUrl())
                .bio(userRequest.bio())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setPassword(userRequest.password());
        user.setAvatarUrl(userRequest.avatarUrl());
        user.setBio(userRequest.bio());

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCreatedAt()
        );
    }
}
