package com.anuar.thejuniorsmind.service;

import com.anuar.thejuniorsmind.dto.CategoryRequestDTO;
import com.anuar.thejuniorsmind.dto.CategoryResponseDTO;
import com.anuar.thejuniorsmind.exception.CategoryNotFoundException;
import com.anuar.thejuniorsmind.model.Category;
import com.anuar.thejuniorsmind.repository.CategoryRepository;
import com.anuar.thejuniorsmind.service.imp.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper);

        requestDTO = new CategoryRequestDTO("Technology", "https://icon.png");
        category = Category.builder()
                .id(1L)
                .name("Technology")
                .iconUrl("https://icon.png")
                .build();
    }

    @Test
    @DisplayName("Should create a category successfully")
    void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO response = categoryService.createCategory(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Technology");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should return category by ID successfully")
    void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.getCategoryById(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Technology");
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when category not found by ID")
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 1");
    }

    @Test
    @DisplayName("Should return all categories")
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Technology");
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Should update a category successfully")
    void testUpdateCategory() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // when
        CategoryResponseDTO response = categoryService.updateCategory(1L, requestDTO);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Technology");
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent category")
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(1L, requestDTO))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 1");
    }

    @Test
    @DisplayName("Should delete a category successfully")
    void testDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 1");
    }

}
