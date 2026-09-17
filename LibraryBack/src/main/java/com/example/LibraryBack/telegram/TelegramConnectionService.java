package com.example.LibraryBack.telegram;

public interface TelegramConnectionService {
    String generateConnectionCode(Long userId);
    void connectTelegram(String code, Long chatId, String username);
    void disconnectTelegram(Long userId);
}