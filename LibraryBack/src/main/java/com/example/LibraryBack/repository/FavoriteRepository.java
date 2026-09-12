package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Favorite;
import com.example.LibraryBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndBook(User user, Book book);
}