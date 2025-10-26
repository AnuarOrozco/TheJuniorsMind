package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;
import com.anuar.thejuniorsmind.exception.TagNotFoundException;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.repository.TagRepository;
import com.anuar.thejuniorsmind.service.imp.TagServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        tag = Tag.builder()
                .id(1L)
                .name("Tech")
                .color("#FF0000")
                .build();

        requestDTO = new TagRequestDTO("Tech", "#FF0000");
    }

    // -------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should create tag successfully")
    void testCreateTagSuccessfully() {
        when(modelMapper.map(requestDTO, Tag.class)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);

        TagResponseDTO response = tagService.createTag(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Tech");
        verify(tagRepository).save(tag);
    }

    // -------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should get tag by id successfully")
    void testGetTagByIdSuccessfully() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        TagResponseDTO response = tagService.getTagById(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Tech");
        verify(tagRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw TagNotFoundException when tag does not exist")
    void testGetTagById_NotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(1L));
    }

    // -------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should get all tags successfully")
    void testGetAllTagsSuccessfully() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));

        List<TagResponseDTO> responses = tagService.getAllTags();

        assertThat(responses).hasSize(1);
        verify(tagRepository).findAll();
    }

    // -------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should update tag successfully")
    void testUpdateTagSuccessfully() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        doNothing().when(modelMapper).map(requestDTO, tag);
        when(tagRepository.save(tag)).thenReturn(tag);

        TagResponseDTO response = tagService.updateTag(1L, requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Tech");
        verify(tagRepository).save(tag);
    }

    @Test
    @DisplayName("Should throw TagNotFoundException when updating non-existent tag")
    void testUpdateTag_NotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(1L, requestDTO));
    }

    // -------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------
    @Test
    @DisplayName("Should delete tag successfully")
    void testDeleteTagSuccessfully() {
        Post post = Post.builder().id(1L).build();
        tag.setPosts(List.of(post));
        post.setTags(new java.util.ArrayList<>(List.of(tag))); // thisss is mutable list

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        assertThat(post.getTags()).doesNotContain(tag);
        verify(tagRepository).delete(tag);
    }

    @Test
    @DisplayName("Should throw TagNotFoundException when deleting non-existent tag")
    void testDeleteTag_NotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(1L));
    }
}
