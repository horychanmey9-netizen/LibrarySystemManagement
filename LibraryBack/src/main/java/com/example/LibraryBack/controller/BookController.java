package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/test")
    public String test() {
        return "BOOK CONTROLLER WORKING";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> create(@ModelAttribute BookRequest bookRequest, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ApiResponse<>("Book created successfully", 201, bookService.create(bookRequest, file));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<List<BookResponse>> getData() {
        return new ApiResponse<>("Get data successfully", 200, bookService.getData()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ApiResponse<>("Book deleted successfully", 200, null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> updateBook(@PathVariable Long id, @ModelAttribute BookRequest bookRequest, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return new ApiResponse<>("Book updated successfully", 200, bookService.updateBook(id, bookRequest, file));
    }
}