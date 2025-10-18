package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.PostRequestDTO;
import com.anuar.thejuniorsmind.dto.PostResponseDTO;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequest);
    PostResponseDTO getPostById(Long id);
    List<PostResponseDTO> getAllPosts();
    PostResponseDTO updatePost(Long id, PostRequestDTO postRequest);
    void deletePost(Long id);
    List<PostResponseDTO> getPostsByTitle(String title);
    List<PostResponseDTO> getPostsByCategory(Long categoryId);
    List<PostResponseDTO> getPostsByTag(String tagName);

}
