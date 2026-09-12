package com.example.LibraryBack.telegram;

public interface TelegramBotService {
    void sendMessage(Long chatId, String message);
}