package com.anuar.thejuniorsmind.service.imp;

import com.anuar.thejuniorsmind.dto.CategoryRequestDTO;
import com.anuar.thejuniorsmind.dto.CategoryResponseDTO;
import com.anuar.thejuniorsmind.exception.CategoryNotFoundException;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.repository.CategoryRepository;
import com.anuar.thejuniorsmind.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        Category category = Category.builder()
                .name(request.name())
                .iconUrl(request.iconUrl())
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return mapToResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setName(request.name());
        category.setIconUrl(request.iconUrl());

        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        return modelMapper.map(category, CategoryResponseDTO.class);
    }
}
