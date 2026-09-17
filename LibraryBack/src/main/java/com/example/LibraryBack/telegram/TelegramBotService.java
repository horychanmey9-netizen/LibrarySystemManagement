package com.example.LibraryBack.telegram;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TelegramBotService {

    void sendMessage(Long chatId, String message);

    void sendBorrowAcceptedNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate
    );

    void sendDueInTwoDaysNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate
    );

    void sendDueTomorrowNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate
    );

    void sendOverdueNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate
    );

    void sendFinePaidNotification(
            Long chatId,
            BigDecimal amount
    );

    void sendBorrowRejectedNotification(
            Long chatId,
            String bookTitle
    );
}