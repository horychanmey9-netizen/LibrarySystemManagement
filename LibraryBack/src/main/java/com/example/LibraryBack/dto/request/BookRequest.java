package com.example.LibraryBack.dto.request;

import com.example.LibraryBack.enums.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private Long categoryId;
    private String title;
    private Integer qty;
    private String description;
    private String author;
    private Integer pages;
    private String isbn;
    private String language;
    private String image;
    private Status status;

}
