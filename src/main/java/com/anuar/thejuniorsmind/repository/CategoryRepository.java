package com.anuar.thejuniorsmind.repository;

import com.anuar.thejuniorsmind.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
