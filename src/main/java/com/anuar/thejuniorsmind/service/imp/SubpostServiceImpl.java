package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.SubpostRequestDTO;
import com.anuar.thejuniorsmind.dto.SubpostResponseDTO;
import com.anuar.thejuniorsmind.exception.SubpostNotFoundException;
import com.anuar.thejuniorsmind.model.Subpost;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.SubpostRepository;
import com.anuar.thejuniorsmind.repository.PostRepository;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.SubpostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubpostServiceImpl implements SubpostService {

    private final SubpostRepository subpostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public SubpostResponseDTO createSubpost(SubpostRequestDTO subpostRequest) {
        Subpost subpost = new Subpost();
        subpost.setSubtitle(subpostRequest.subtitle());
        subpost.setContent(subpostRequest.content());

        User author = userRepository.findById(subpostRequest.authorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + subpostRequest.authorId()));
        subpost.setAuthor(author);

        Post post = postRepository.findById(subpostRequest.postId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + subpostRequest.postId()));
        subpost.setPost(post);

        Subpost saved = subpostRepository.save(subpost);
        return mapToResponseDTO(saved);
    }

    @Override
    public SubpostResponseDTO getSubpostById(Long id) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));
        return mapToResponseDTO(subpost);
    }

    @Override
    public List<SubpostResponseDTO> getAllSubposts() {
        return subpostRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubpostResponseDTO updateSubpost(Long id, SubpostRequestDTO subpostRequest) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));

        subpost.setSubtitle(subpostRequest.subtitle());
        subpost.setContent(subpostRequest.content());

        if (subpostRequest.authorId() != null && !subpost.getAuthor().getId().equals(subpostRequest.authorId())) {
            User author = userRepository.findById(subpostRequest.authorId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + subpostRequest.authorId()));
            subpost.setAuthor(author);
        }

        if (subpostRequest.postId() != null && !subpost.getPost().getId().equals(subpostRequest.postId())) {
            Post post = postRepository.findById(subpostRequest.postId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + subpostRequest.postId()));
            subpost.setPost(post);
        }

        Subpost updated = subpostRepository.save(subpost);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteSubpost(Long id) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));

        subpostRepository.delete(subpost);
    }

    private SubpostResponseDTO mapToResponseDTO(Subpost subpost) {
        return new SubpostResponseDTO(
                subpost.getId(),
                subpost.getSubtitle(),
                subpost.getContent(),
                subpost.getCreatedAt(),
                subpost.getAuthor().getId(),
                subpost.getPost().getId()
        );
    }
}
