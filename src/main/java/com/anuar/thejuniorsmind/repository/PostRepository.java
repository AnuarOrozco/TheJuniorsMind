package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingIgnoreCase(String title);
    List<Post> findByCategoryId(Long categoryId);
    List<Post> findByTagsName(String name);

}
