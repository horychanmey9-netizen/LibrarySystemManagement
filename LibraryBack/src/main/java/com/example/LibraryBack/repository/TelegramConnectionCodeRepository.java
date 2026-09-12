package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.TelegramConnectionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramConnectionCodeRepository extends JpaRepository<TelegramConnectionCode, Long> {
    Optional<TelegramConnectionCode> findByCode(String code);
    Optional<TelegramConnectionCode> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}