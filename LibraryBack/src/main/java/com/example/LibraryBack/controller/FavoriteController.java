package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.FavoriteResponse;
import com.example.LibraryBack.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;


    @PostMapping("/{bookId}")
    public ApiResponse<FavoriteResponse> create(
            @PathVariable Long bookId
    ) {

        FavoriteResponse favoriteResponse =
                favoriteService.create(bookId);

        return ApiResponse.<FavoriteResponse>builder()
                .status(201)
                .msg("Book added to favorite successfully")
                .data(favoriteResponse)
                .build();
    }


    @GetMapping
    public ApiResponse<List<FavoriteResponse>> getData() {

        List<FavoriteResponse> response =
                favoriteService.getData();

        return ApiResponse.<List<FavoriteResponse>>builder()
                .status(200)
                .msg("Get favorite books successfully")
                .data(response)
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ApiResponse.<Void>builder()
                .status(200)
                .msg("Favorite deleted successfully")
                .build();
    }
}