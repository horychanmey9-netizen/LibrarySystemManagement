package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.response.FavoriteResponse;
import com.example.LibraryBack.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "title")
    @Mapping(source = "book.author", target = "author")
    @Mapping(source = "book.image", target = "image")
    FavoriteResponse toResponse(Favorite favorite);
}