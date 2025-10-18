package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;

import java.util.List;

public interface TagService {

    TagResponseDTO createTag(TagRequestDTO tagRequest);
    TagResponseDTO getTagById(Long id);
    List<TagResponseDTO> getAllTags();
    TagResponseDTO updateTag(Long id, TagRequestDTO tagRequest);
    void deleteTag(Long id);

}
