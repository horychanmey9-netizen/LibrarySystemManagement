package com.example.LibraryBack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private String title;
    private String author;
    private String image;
}
