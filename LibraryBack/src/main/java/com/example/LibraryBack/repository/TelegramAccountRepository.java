package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {
    Optional<TelegramAccount> findByUser_Id(Long userId);
    Optional<TelegramAccount> findByChatId(Long chatId);
    boolean existsByChatId(Long chatId);
    boolean existsByUser_Id(Long userId);
}