package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
