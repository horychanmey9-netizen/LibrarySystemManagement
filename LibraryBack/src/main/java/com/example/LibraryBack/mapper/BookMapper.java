package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequest bookRequest);
    BookResponse bookResponse(Book book);
    List<BookResponse> toResponseList(List<Book> books);
    void updateBook(BookRequest bookRequest, @MappingTarget Book book);
}
