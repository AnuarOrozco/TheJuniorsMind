package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.SubpostRequestDTO;
import com.anuar.thejuniorsmind.dto.SubpostResponseDTO;

import java.util.List;

public interface SubpostService {

    SubpostResponseDTO createSubpost(SubpostRequestDTO subpostRequest);
    SubpostResponseDTO getSubpostById(Long id);
    List<SubpostResponseDTO> getAllSubposts();
    SubpostResponseDTO updateSubpost(Long id, SubpostRequestDTO subpostRequest);
    void deleteSubpost(Long id);
}
