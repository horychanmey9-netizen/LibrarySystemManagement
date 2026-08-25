package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.BookMapper;
import com.example.LibraryBack.repositoy.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile file) throws IOException {
        if (bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new NotException("ISBN already exists");
        }

        Book book = bookMapper.toEntity(bookRequest);
        if (file == null || file.isEmpty()) {
            throw new NotException("Book image is required!");
        }

        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFileName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        String imageUrl = "http://localhost:8080/uploads/" + fileName;
        book.setImage(imageUrl);
        Book savedBook = bookRepository.save(book);
        return bookMapper.bookResponse(savedBook);
    }
    @Override
    public List<BookResponse> getData() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toResponseList(books);
    }
    @Override
    public BookResponse updateBook(Long id, BookRequest bookRequest, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotException("Book not found!!!"));
        bookMapper.updateBook(bookRequest, book);

        if (file != null && !file.isEmpty()) {

            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            String imageUrl = "http://localhost:8080/uploads/" + fileName;

            book.setImage(imageUrl);
        }
        Book updatedBook = bookRepository.save(book);

        return bookMapper.bookResponse(updatedBook);
    }
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotException("Book not found!!!"));
        bookRepository.delete(book);
    }
}