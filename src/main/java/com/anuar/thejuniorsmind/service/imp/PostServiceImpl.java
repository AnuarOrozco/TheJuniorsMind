package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.PostRequestDTO;
import com.anuar.thejuniorsmind.dto.PostResponseDTO;
import com.anuar.thejuniorsmind.exception.CategoryNotFoundException;
import com.anuar.thejuniorsmind.exception.PostNotFoundException;
import com.anuar.thejuniorsmind.exception.UserNotFoundException;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.model.Post;
import com.anuar.thejuniorsmind.model.Tag;
import com.anuar.thejuniorsmind.model.User;
import com.anuar.thejuniorsmind.repository.CategoryRepository;
import com.anuar.thejuniorsmind.repository.PostRepository;
import com.anuar.thejuniorsmind.repository.TagRepository;
import com.anuar.thejuniorsmind.repository.UserRepository;
import com.anuar.thejuniorsmind.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequest) {
        User author = userRepository.findById(postRequest.authorId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + postRequest.authorId()));

        Category category = categoryRepository.findById(postRequest.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + postRequest.categoryId()));
        
        List<Tag> tags = tagRepository.findAllById(postRequest.tagIds());
        
        Post post = Post.builder()
                .title(postRequest.title())
                .content(postRequest.content())
                .author(author)
                .category(category)
                .tags(tags)
                .build();
        
        Post saved = postRepository.save(post);
        return mapToResponseDTO(saved);
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return mapToResponseDTO(post);
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());

        Category category = categoryRepository.findById(postRequest.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + postRequest.categoryId()));
        post.setCategory(category);

        List<Tag> tags = tagRepository.findAllById(postRequest.tagIds());
        post.setTags(tags);

        Post updated = postRepository.save(post);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
    }

    @Override
    public List<PostResponseDTO> getPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByCategory(Long categoryId) {
        return postRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByTag(String tagName) {
        return postRepository.findByTagsName(tagName)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO mapToResponseDTO(Post post) {
        List<String> tagNames = post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getAuthor().getId(),
                post.getCategory().getId(),
                tagNames
        );
    }
}
