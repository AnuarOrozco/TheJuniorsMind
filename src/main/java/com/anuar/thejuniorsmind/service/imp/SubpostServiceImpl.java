package com.anuar.thejuniorsmind.service.imp;

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
import com.anuar.thejuniorsmind.service.SubpostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final SubpostMapper subpostMapper;

    @Override
    public SubpostResponseDTO createSubpost(SubpostRequestDTO subpostRequest) {
        User author = userRepository.findById(subpostRequest.authorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + subpostRequest.authorId()));

        Post post = postRepository.findById(subpostRequest.postId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + subpostRequest.postId()));

        Subpost subpost = subpostMapper.toEntity(subpostRequest, author, post);
        Subpost saved = subpostRepository.save(subpost);

        return subpostMapper.toResponseDTO(saved);
    }

    @Override
    public SubpostResponseDTO getSubpostById(Long id) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));

        return subpostMapper.toResponseDTO(subpost);
    }

    @Override
    public List<SubpostResponseDTO> getAllSubposts() {
        return subpostRepository.findAll()
                .stream()
                .map(subpostMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubpostResponseDTO updateSubpost(Long id, SubpostRequestDTO subpostRequest) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));

        User author = null;
        Post post = null;

        if (subpostRequest.authorId() != null && !subpost.getAuthor().getId().equals(subpostRequest.authorId())) {
            author = userRepository.findById(subpostRequest.authorId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + subpostRequest.authorId()));
        }

        if (subpostRequest.postId() != null && !subpost.getPost().getId().equals(subpostRequest.postId())) {
            post = postRepository.findById(subpostRequest.postId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + subpostRequest.postId()));
        }

        subpostMapper.updateEntity(subpost, subpostRequest, author, post);
        Subpost updated = subpostRepository.save(subpost);

        return subpostMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteSubpost(Long id) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new SubpostNotFoundException("Subpost not found with id: " + id));

        subpostRepository.delete(subpost);
    }
}