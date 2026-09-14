package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.response.FavoriteResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Favorite;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.mapper.FavoriteMapper;
import com.example.LibraryBack.repository.BookRepository;
import com.example.LibraryBack.repository.FavoriteRepository;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

    @Override
    public FavoriteResponse create(Long bookId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        if (favoriteRepository
                .findByUserAndBook(user, book)
                .isPresent()) {

            throw new RuntimeException("Book already in favorite");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .book(book)
                .build();

        favoriteRepository.save(favorite);

        return favoriteMapper.toResponse(favorite);
    }

    @Override
    public List<FavoriteResponse> getData() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Favorite> favorites =
                favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(favoriteMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteFavorite(Long bookId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        Favorite favorite = favoriteRepository
                .findByUserAndBook(user, book)
                .orElseThrow(() ->
                        new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }
}