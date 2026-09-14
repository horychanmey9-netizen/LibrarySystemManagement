package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {
    FavoriteResponse create(Long bookId);
    List<FavoriteResponse> getData();
    void deleteFavorite(Long bookId);
}