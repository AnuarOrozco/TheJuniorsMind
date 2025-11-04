package com.anuar.thejuniorsmind.mapper;

import com.anuar.thejuniorsmind.dto.UserRequestDTO;
import com.anuar.thejuniorsmind.dto.UserResponseDTO;
import com.anuar.thejuniorsmind.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .avatarUrl(dto.avatarUrl())
                .bio(dto.bio())
                .build();
    }

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCreatedAt()
        );
    }

    public void updateEntityFromDTO(UserRequestDTO dto, User user) {
        if (dto == null || user == null) {
            return;
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setAvatarUrl(dto.avatarUrl());
        user.setBio(dto.bio());
    }
}