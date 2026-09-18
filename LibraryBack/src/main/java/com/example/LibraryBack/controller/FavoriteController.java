package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.FavoriteRequest;
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


    @PostMapping
    public ApiResponse<FavoriteResponse> create(
            @RequestBody FavoriteRequest request
    ) {

        FavoriteResponse favoriteResponse =
                favoriteService.create(request.getBookId());

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


    @DeleteMapping("/{bookId}")
    public ApiResponse<Void> deleteFavorite(
            @PathVariable Long bookId
    ) {

        favoriteService.deleteFavorite(bookId);

        return ApiResponse.<Void>builder()
                .status(200)
                .msg("Favorite deleted successfully")
                .build();
    }
}