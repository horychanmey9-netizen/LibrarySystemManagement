package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.CategoryRequest;
import com.example.LibraryBack.dto.response.CategoryResponse;
import com.example.LibraryBack.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest dto);
    CategoryResponse toResponse(Category entity);
}
