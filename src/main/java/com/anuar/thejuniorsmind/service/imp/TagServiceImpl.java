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
        return mapToResponseDTO(saved);
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));
        return mapToResponseDTO(tag);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        modelMapper.map(tagRequest, tag); // actualizar campos básicos

        // Si TagRequestDTO en un futuro tuviese postIds, aca van mapeados :):
        // if (tagRequest.getPostIds() != null) {
        //     List<Post> posts = postRepository.findAllById(tagRequest.getPostIds());
        //     tag.setPosts(posts);
        // }

        Tag updated = tagRepository.save(tag);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with id: " + id));

        if (tag.getPosts() != null) {
            tag.getPosts().forEach(post -> post.getTags().remove(tag));
        }

        tagRepository.delete(tag);
    }

    private TagResponseDTO mapToResponseDTO(Tag tag) {
        List<Long> postIds = tag.getPosts() != null
                ? tag.getPosts().stream().map(post -> post.getId()).collect(Collectors.toList())
                : List.of();

        return new TagResponseDTO(
                tag.getId(),
                tag.getName(),
                tag.getColor(),
                postIds
        );
    }

}
