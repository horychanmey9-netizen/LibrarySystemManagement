package com.example.LibraryBack.telegram;

import com.example.LibraryBack.dto.response.TelegramConnectionResponse;

public interface TelegramConnectionService {

    TelegramConnectionResponse generateConnectionCode(Long userId);
    void connectTelegram(String code, Long chatId, String username);
    void disconnectTelegram(Long userId);
}