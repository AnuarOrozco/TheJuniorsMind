package com.anuar.thejuniorsmind.controller;

import com.anuar.thejuniorsmind.dto.PostRequestDTO;
import com.anuar.thejuniorsmind.dto.PostResponseDTO;
import com.anuar.thejuniorsmind.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO request) {
        PostResponseDTO created = postService.createPost(request);

        return ResponseEntity.created(URI.create("/api/posts/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO response = postService.getPostById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
                                                      @Valid @RequestBody PostRequestDTO request) {
        PostResponseDTO updated = postService.updatePost(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> getPostByTitle(@RequestParam String title) {
        List<PostResponseDTO> posts = postService.getPostsByTitle(title);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostResponseDTO>> getPostByCategory(@PathVariable Long categoryId) {
        List<PostResponseDTO> posts = postService.getPostsByCategory(categoryId);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<List<PostResponseDTO>> getPostByTag(@PathVariable String tagName) {
        List<PostResponseDTO> posts = postService.getPostsByTag(tagName);

        return ResponseEntity.ok(posts);
    }

}
