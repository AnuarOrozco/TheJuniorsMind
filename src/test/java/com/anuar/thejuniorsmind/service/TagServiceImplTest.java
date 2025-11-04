package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.TagRequestDTO;
import com.anuar.thejuniorsmind.dto.TagResponseDTO;
import com.anuar.thejuniorsmind.exception.TagNotFoundException;
import com.anuar.thejuniorsmind.mapper.TagMapper;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.repository.TagRepository;
import com.anuar.thejuniorsmind.service.imp.TagServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagRequestDTO requestDTO;
    private TagResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        tag = Tag.builder()
                .id(1L)
                .name("Tech")
                .color("#FF0000")
                .build();

        requestDTO = new TagRequestDTO("Tech", "#FF0000");
        responseDTO = new TagResponseDTO(1L, "Tech", "#FF0000", List.of());
    }

    @Test
    @DisplayName("Should create tag successfully")
    void testCreateTagSuccessfully() {
        when(tagMapper.toEntity(requestDTO)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        TagResponseDTO response = tagService.createTag(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Tech");
        verify(tagRepository).save(tag);
    }

    @Test
    @DisplayName("Should get tag by id successfully")
    void testGetTagByIdSuccessfully() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

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

    @Test
    @DisplayName("Should get all tags successfully")
    void testGetAllTagsSuccessfully() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        List<TagResponseDTO> responses = tagService.getAllTags();

        assertThat(responses).hasSize(1);
        verify(tagRepository).findAll();
    }

    @Test
    @DisplayName("Should update tag successfully")
    void testUpdateTagSuccessfully() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        doAnswer(invocation -> {
            TagRequestDTO dto = invocation.getArgument(0);
            Tag entity = invocation.getArgument(1);
            entity.setName(dto.name());
            entity.setColor(dto.color());
            return null;
        }).when(tagMapper).updateEntityFromDTO(requestDTO, tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

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

    @Test
    @DisplayName("Should delete tag successfully")
    void testDeleteTagSuccessfully() {
        Post post = Post.builder().id(1L).build();
        tag.setPosts(List.of(post));
        post.setTags(new java.util.ArrayList<>(List.of(tag)));

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