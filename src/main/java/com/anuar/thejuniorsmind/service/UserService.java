package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.UserRequestDTO;
import com.anuar.thejuniorsmind.dto.UserResponseDTO;
import com.anuar.thejuniorsmind.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequest);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);
    void deleteUser(Long id);
    List<UserResponseDTO> getAllUsers();

}
