package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    // =====================================================
    // REQUEST -> ENTITY
    // =====================================================

    @Mapping(
            target = "publishYear",
            source = "publishYear"
    )
    Book toEntity(BookRequest bookRequest);


    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    @Mapping(
            target = "categoryId",
            source = "category.id"
    )
    @Mapping(
            target = "category",
            source = "category.name"
    )
    @Mapping(
            target = "publishYear",
            source = "publishYear"
    )
    BookResponse bookResponse(Book book);


    // =====================================================
    // LIST ENTITY -> RESPONSE
    // =====================================================

    List<BookResponse> toResponseList(List<Book> books);


    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @Mapping(
            target = "publishYear",
            source = "publishYear"
    )
    void updateBook(
            BookRequest bookRequest,
            @MappingTarget Book book
    );
}