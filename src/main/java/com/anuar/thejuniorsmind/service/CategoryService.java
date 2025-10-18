package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.CategoryRequestDTO;
import com.anuar.thejuniorsmind.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequest);
    CategoryResponseDTO getCategoryById(Long id);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequest);
    void deleteCategory(Long id);

}
