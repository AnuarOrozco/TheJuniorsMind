package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.CategoryRequestDTO;
import com.anuar.thejuniorsmind.dto.CategoryResponseDTO;
import com.anuar.thejuniorsmind.exception.CategoryNotFoundException;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.repository.CategoryRepository;
import com.anuar.thejuniorsmind.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);
        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return mapToResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        modelMapper.map(categoryRequest, category);

        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        // Limpiar relación con posts antes de borrar (orphanRemoval ya maneja parte, pero por seguridad)
        if (category.getPosts() != null) {
            category.getPosts().forEach(post -> post.setCategory(null));
        }

        categoryRepository.delete(category);
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        List<Long> postIds = category.getPosts() != null
                ? category.getPosts().stream().map(post -> post.getId()).collect(Collectors.toList())
                : List.of();

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getIconUrl(),
                postIds
        );
    }
}
