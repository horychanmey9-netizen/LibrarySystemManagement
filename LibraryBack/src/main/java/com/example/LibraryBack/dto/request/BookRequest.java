package com.example.LibraryBack.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String title;
    private Integer qty;
    private String description;
    private String author;
    private Integer pages;
    private String isbn;
    private String language;
    private String image;
    private boolean status;
}
