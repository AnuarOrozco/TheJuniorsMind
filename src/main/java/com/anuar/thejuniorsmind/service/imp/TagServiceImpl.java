package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;
import com.anuar.thejuniorsmind.exception.TagNotFoundException;
import com.anuar.thejuniorsmind.mapper.TagMapper;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.repository.TagRepository;
import com.anuar.thejuniorsmind.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequest) {
        Tag tag = tagMapper.toEntity(tagRequest);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toResponseDTO(saved);
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));
        return tagMapper.toResponseDTO(tag);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        tagMapper.updateEntityFromDTO(tagRequest, tag);

        Tag updated = tagRepository.save(tag);
        return tagMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        // Desvincular el tag de sus posts antes de eliminar
        if (tag.getPosts() != null) {
            tag.getPosts().forEach(post -> post.getTags().remove(tag));
        }

        tagRepository.delete(tag);
    }
}