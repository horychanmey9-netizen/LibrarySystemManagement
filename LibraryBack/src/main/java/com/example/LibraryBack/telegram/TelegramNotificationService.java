package com.example.LibraryBack.telegram;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TelegramNotificationService {
    void sendBorrowAcceptedNotification(Long userId, String bookTitle, LocalDate dueDate);
    void sendDueInTwoDaysNotification(Long userId, String bookTitle, LocalDate dueDate);
    void sendDueTomorrowNotification(Long userId, String bookTitle, LocalDate dueDate);
    void sendOverdueNotification(Long userId, String bookTitle, LocalDate dueDate);
    void sendFinePaidNotification(Long userId, String bookTitle, BigDecimal fineAmount);
    void sendBorrowRejectedNotification(Long userId, String bookTitle);
}