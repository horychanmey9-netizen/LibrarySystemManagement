package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    @Mapping(
            target = "publicationYear",
            source = "publicationYear"
    )
    Book toEntity(BookRequest bookRequest);

    @Mapping(
            target = "categoryId",
            source = "category.id"
    )
    @Mapping(
            target = "category",
            source = "category.name"
    )
    @Mapping(
            target = "publicationYear",
            source = "publicationYear"
    )
    BookResponse bookResponse(Book book);

    List<BookResponse> toResponseList(List<Book> books);

    @Mapping(
            target = "category",
            ignore = true
    )
    @Mapping(
            target = "image",
            ignore = true
    )
    @Mapping(
            target = "publicationYear",
            source = "publicationYear"
    )
    void updateBook(
            BookRequest bookRequest,
            @MappingTarget Book book
    );
}