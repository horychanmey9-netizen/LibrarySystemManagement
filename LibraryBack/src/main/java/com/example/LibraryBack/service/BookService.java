package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    BookResponse create(BookRequest bookRequest, MultipartFile file)throws IOException;
    List<BookResponse> getData();
    void deleteBook(Long id);
    BookResponse updateBook(Long id,BookRequest bookRequest, MultipartFile file)throws IOException;
}
