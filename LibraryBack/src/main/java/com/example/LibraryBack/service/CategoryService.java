package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.CategoryRequest;
import com.example.LibraryBack.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest);
    List<CategoryResponse> getData();
    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long id);
}
