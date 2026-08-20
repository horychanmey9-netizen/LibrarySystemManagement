package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.CategoryRequest;
import com.example.LibraryBack.dto.response.CategoryResponse;
import com.example.LibraryBack.entity.Category;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.CategoryMapper;
import com.example.LibraryBack.repositoy.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getData() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotException("Category not found with id: " + id));
        existingCategory.setName(categoryRequest.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
