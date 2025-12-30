package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.SubpostRequestDTO;
import com.anuar.thejuniorsmind.dto.SubpostResponseDTO;
import com.anuar.thejuniorsmind.exception.SubpostNotFoundException;
import com.anuar.thejuniorsmind.mapper.SubpostMapper;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Subpost;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.PostRepository;
import com.anuar.thejuniorsmind.repository.SubpostRepository;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.imp.SubpostServiceImpl;
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
class SubpostServiceImplTest {

    @Mock
    private SubpostRepository subpostRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    private SubpostMapper subpostMapper;

    @InjectMocks
    private SubpostServiceImpl subpostService;

    private User author;
    private Post post;
    private Subpost subpost;
    private SubpostRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        subpostMapper = new SubpostMapper();

        // Reinyectar el mapper en el servicio (ya que no se mockea)
        subpostService = new SubpostServiceImpl(subpostRepository, userRepository, postRepository, subpostMapper);

        author = User.builder().id(1L).username("John").build();
        post = Post.builder().id(1L).title("My Post").build();

        subpost = Subpost.builder()
                .id(1L)
                .subtitle("Subpost Title")
                .content("Subpost Content")
                .author(author)
                .post(post)
                .build();

        requestDTO = new SubpostRequestDTO(
                "Subpost Title",
                "Subpost Content",
                1L,
                1L
        );
    }

    @Test
    @DisplayName("Should create subpost successfully")
    void testCreateSubpostSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(subpostRepository.save(any(Subpost.class))).thenReturn(subpost);

        SubpostResponseDTO response = subpostService.createSubpost(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.subtitle()).isEqualTo("Subpost Title");
        assertThat(response.authorId()).isEqualTo(1L);
        assertThat(response.postId()).isEqualTo(1L);
        verify(subpostRepository).save(any(Subpost.class));
    }

    @Test
    @DisplayName("Should get subpost by id successfully")
    void testGetSubpostByIdSuccessfully() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.of(subpost));

        SubpostResponseDTO response = subpostService.getSubpostById(1L);

        assertThat(response).isNotNull();
        assertThat(response.subtitle()).isEqualTo("Subpost Title");
        verify(subpostRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw SubpostNotFoundException when subpost does not exist")
    void testGetSubpostById_NotFound() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SubpostNotFoundException.class, () -> subpostService.getSubpostById(1L));
    }

    @Test
    @DisplayName("Should get all subposts successfully")
    void testGetAllSubpostsSuccessfully() {
        when(subpostRepository.findAll()).thenReturn(List.of(subpost));

        List<SubpostResponseDTO> responses = subpostService.getAllSubposts();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).subtitle()).isEqualTo("Subpost Title");
        verify(subpostRepository).findAll();
    }

    @Test
    @DisplayName("Should update subpost successfully")
    void testUpdateSubpostSuccessfully() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.of(subpost));
        when(subpostRepository.save(subpost)).thenReturn(subpost);

        SubpostResponseDTO response = subpostService.updateSubpost(1L, requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.subtitle()).isEqualTo("Subpost Title");
        verify(subpostRepository).save(subpost);
    }

    @Test
    @DisplayName("Should throw SubpostNotFoundException when updating non-existent subpost")
    void testUpdateSubpost_NotFound() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SubpostNotFoundException.class, () -> subpostService.updateSubpost(1L, requestDTO));
    }

    @Test
    @DisplayName("Should delete subpost successfully")
    void testDeleteSubpostSuccessfully() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.of(subpost));

        subpostService.deleteSubpost(1L);

        verify(subpostRepository).delete(subpost);
    }

    @Test
    @DisplayName("Should throw SubpostNotFoundException when deleting non-existent subpost")
    void testDeleteSubpost_NotFound() {
        when(subpostRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SubpostNotFoundException.class, () -> subpostService.deleteSubpost(1L));
    }
}
