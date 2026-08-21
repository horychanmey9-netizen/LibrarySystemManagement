package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.CategoryRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.CategoryResponse;
import com.example.LibraryBack.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.create(categoryRequest);
        return ApiResponse.<CategoryResponse>builder()
                .status(201)
                .msg("Category created successfully")
                .data(categoryResponse)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getData() {

        List<CategoryResponse> response = categoryService.getData();

        return ApiResponse.<List<CategoryResponse>>builder()
                .status(200)
                .msg("Get categories successfully")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
        return ApiResponse.<CategoryResponse>builder()
                .status(200)
                .msg("Category updated successfully")
                .data(categoryResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .status(200)
                .msg("Category deleted successfully")
                .build();
    }
}
