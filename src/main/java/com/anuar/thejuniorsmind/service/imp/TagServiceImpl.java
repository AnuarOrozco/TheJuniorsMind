package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;
import com.anuar.thejuniorsmind.exception.TagNotFoundException;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.repository.TagRepository;
import com.anuar.thejuniorsmind.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequest) {
        Tag tag = modelMapper.map(tagRequest, Tag.class);
        Tag saved = tagRepository.save(tag);
        return modelMapper.map(saved, TagResponseDTO.class);
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));
        return modelMapper.map(tag, TagResponseDTO.class);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        return  tagRepository.findAll()
                .stream()
                .map(tag -> modelMapper.map(tag, TagResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        modelMapper.map(tagRequest, tag);
        Tag updated = tagRepository.save(tag);
        return modelMapper.map(updated, TagResponseDTO.class);
    }

    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
