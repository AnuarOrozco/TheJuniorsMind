package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
