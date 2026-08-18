package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.BookMapper;
import com.example.LibraryBack.repositoy.BookRepository;
import com.example.LibraryBack.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    @Override
    public BookResponse create(BookRequest bookRequest, MultipartFile file) throws IOException {

        Book book = bookMapper.toEntity(bookRequest);
        String fileName = file.getOriginalFilename();
        String fileUrl = UUID.randomUUID() + "_" + fileName;
        Path path = Paths.get("uploads");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(), path.resolve(fileUrl));
        String imageUrl = "http://localhost:8080/uploads/" + fileUrl;
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

        String fileName = file.getOriginalFilename();
        String fileUrl = UUID.randomUUID() + "_" + fileName;
        Path path = Paths.get("uploads");

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(file.getInputStream(), path.resolve(fileUrl));
        String imageUrl = "http://localhost:8080/uploads/" + fileUrl;
        book.setImage(imageUrl);
        Book book1 = bookRepository.save(book);
        return bookMapper.bookResponse(book1);
    }
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotException("Book not found!!!"));
        bookRepository.delete(book);
    }
}
