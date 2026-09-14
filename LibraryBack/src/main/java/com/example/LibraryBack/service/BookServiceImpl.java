package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BookRequest;
import com.example.LibraryBack.dto.response.BookResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Category;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.BookMapper;
import com.example.LibraryBack.repository.BookRepository;
import com.example.LibraryBack.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;


    // =====================================================
    // CREATE BOOK
    // =====================================================

    @Override
    public BookResponse create(
            BookRequest bookRequest,
            MultipartFile file
    ) throws IOException {

        // =================================================
        // CHECK ISBN
        // =================================================

        if (
                bookRequest.getIsbn() != null &&
                        !bookRequest.getIsbn().isBlank() &&
                        bookRepository.existsByIsbn(
                                bookRequest.getIsbn()
                        )
        ) {

            throw new NotException(
                    "ISBN already exists"
            );
        }


        // =================================================
        // DEBUG PUBLICATION YEAR
        // =================================================

        System.out.println(
                "REQUEST YEAR = "
                        + bookRequest.getPublicationYear()
        );


        // =================================================
        // CREATE ENTITY
        // =================================================

        Book book =
                bookMapper.toEntity(
                        bookRequest
                );


        System.out.println(
                "ENTITY YEAR = "
                        + book.getPublicationYear()
        );


        // =================================================
        // IMAGE REQUIRED
        // =================================================

        if (
                file == null ||
                        file.isEmpty()
        ) {

            throw new NotException(
                    "Book image is required!"
            );
        }


        // =================================================
        // FIND CATEGORY
        // =================================================

        if (
                bookRequest.getCategoryId() == null
        ) {

            throw new NotException(
                    "Category is required"
            );
        }


        Category category =
                categoryRepository.findById(
                                bookRequest.getCategoryId()
                        )
                        .orElseThrow(
                                () -> new NotException(
                                        "Category not found"
                                )
                        );


        book.setCategory(
                category
        );


        // =================================================
        // CREATE UPLOAD FOLDER
        // =================================================

        Path uploadPath =
                Paths.get("uploads");


        if (
                !Files.exists(uploadPath)
        ) {

            Files.createDirectories(
                    uploadPath
            );
        }


        // =================================================
        // SAVE IMAGE
        // =================================================

        String originalFileName =
                file.getOriginalFilename();


        if (
                originalFileName == null ||
                        originalFileName.isBlank()
        ) {

            originalFileName =
                    "book-image";
        }


        String fileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;


        Path filePath =
                uploadPath.resolve(
                        fileName
                );


        Files.copy(
                file.getInputStream(),
                filePath
        );


        String imageUrl =
                "http://localhost:8080/uploads/"
                        + fileName;


        book.setImage(
                imageUrl
        );


        // =================================================
        // SAVE BOOK
        // =================================================

        Book savedBook =
                bookRepository.save(
                        book
                );


        System.out.println(
                "SAVED YEAR = "
                        + savedBook.getPublicationYear()
        );


        return bookMapper.bookResponse(
                savedBook
        );
    }


    // =====================================================
    // GET ALL BOOKS
    // =====================================================

    @Override
    public List<BookResponse> getData() {

        List<Book> books =
                bookRepository.findAll();


        return bookMapper.toResponseList(
                books
        );
    }


    // =====================================================
    // UPDATE BOOK
    // =====================================================

    @Override
    public BookResponse updateBook(
            Long id,
            BookRequest bookRequest,
            MultipartFile file
    ) throws IOException {


        // =================================================
        // FIND EXISTING BOOK
        // =================================================

        Book book =
                bookRepository.findById(id)
                        .orElseThrow(
                                () -> new NotException(
                                        "Book not found!!!"
                                )
                        );


        // =================================================
        // CHECK ISBN
        //
        // If user changes ISBN, make sure the new ISBN
        // does not belong to another book.
        // =================================================

        if (
                bookRequest.getIsbn() != null &&
                        !bookRequest.getIsbn().isBlank() &&
                        !bookRequest.getIsbn().equals(
                                book.getIsbn()
                        )
        ) {

            if (
                    bookRepository.existsByIsbn(
                            bookRequest.getIsbn()
                    )
            ) {

                throw new NotException(
                        "ISBN already exists"
                );
            }
        }


        // =================================================
        // UPDATE NORMAL FIELDS
        //
        // BookMapper is configured with:
        //
        // NullValuePropertyMappingStrategy.IGNORE
        //
        // So null fields will NOT overwrite old data.
        //
        // category and image are ignored by mapper because
        // we handle them separately below.
        // =================================================

        bookMapper.updateBook(
                bookRequest,
                book
        );


        // =================================================
        // UPDATE CATEGORY
        // =================================================

        if (
                bookRequest.getCategoryId() != null
        ) {

            Category category =
                    categoryRepository.findById(
                                    bookRequest.getCategoryId()
                            )
                            .orElseThrow(
                                    () -> new NotException(
                                            "Category not found"
                                    )
                            );


            book.setCategory(
                    category
            );
        }


        // =================================================
        // UPDATE IMAGE
        //
        // IMPORTANT:
        //
        // If no new image is selected:
        //     DO NOTHING
        //
        // Therefore old image remains.
        //
        // If a new image is selected:
        //     Save new image
        //     Set new image URL
        // =================================================

        if (
                file != null &&
                        !file.isEmpty()
        ) {


            // =============================================
            // CREATE UPLOAD FOLDER
            // =============================================

            Path uploadPath =
                    Paths.get("uploads");


            if (
                    !Files.exists(uploadPath)
            ) {

                Files.createDirectories(
                        uploadPath
                );
            }


            // =============================================
            // FILE NAME
            // =============================================

            String originalFileName =
                    file.getOriginalFilename();


            if (
                    originalFileName == null ||
                            originalFileName.isBlank()
            ) {

                originalFileName =
                        "book-image";
            }


            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;


            Path filePath =
                    uploadPath.resolve(
                            fileName
                    );


            // =============================================
            // SAVE NEW IMAGE
            // =============================================

            Files.copy(
                    file.getInputStream(),
                    filePath
            );


            // =============================================
            // CREATE IMAGE URL
            // =============================================

            String imageUrl =
                    "http://localhost:8080/uploads/"
                            + fileName;


            // =============================================
            // SET NEW IMAGE
            // =============================================

            book.setImage(
                    imageUrl
            );
        }


        // =================================================
        // SAVE UPDATED BOOK
        // =================================================

        Book updatedBook =
                bookRepository.save(
                        book
                );


        // =================================================
        // RETURN UPDATED BOOK
        // =================================================

        return bookMapper.bookResponse(
                updatedBook
        );
    }


    // =====================================================
    // DELETE BOOK
    // =====================================================

    @Override
    public void deleteBook(
            Long id
    ) {

        Book book =
                bookRepository.findById(id)
                        .orElseThrow(
                                () -> new NotException(
                                        "Book not found!!!"
                                )
                        );


        bookRepository.delete(
                book
        );
    }
}