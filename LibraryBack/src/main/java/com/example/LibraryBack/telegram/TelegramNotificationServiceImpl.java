package com.example.LibraryBack.telegram;

import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TelegramNotificationServiceImpl
        implements TelegramNotificationService {

    private final TelegramAccountRepository telegramAccountRepository;
    private final TelegramBotService telegramBotService;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd MMMM yyyy");

    @Override
    public void sendBorrowAcceptedNotification(Long userId, String bookTitle, LocalDate dueDate) {
        String message =
                "Your borrowing request has been accepted.\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Due date: " + formatDate(dueDate);

        sendToUser(userId, message);
    }

    @Override
    public void sendDueInTwoDaysNotification(
            Long userId,
            String bookTitle,
            LocalDate dueDate
    ) {

        String message =
                "Reminder\n\n"
                        + "Your book '" + bookTitle
                        + "' is due in 2 days.\n"
                        + "Due date: " + formatDate(dueDate);

        sendToUser(userId, message);
    }

    @Override
    public void sendDueTomorrowNotification(
            Long userId,
            String bookTitle,
            LocalDate dueDate
    ) {

        String message =
                "Reminder\n\n"
                        + "Your book '" + bookTitle
                        + "' is due tomorrow.\n"
                        + "Due date: " + formatDate(dueDate)
                        + ".\n\n"
                        + "Please return it on time.";

        sendToUser(userId, message);
    }

    @Override
    public void sendOverdueNotification(
            Long userId,
            String bookTitle,
            LocalDate dueDate
    ) {

        String message =
                "OVERDUE\n\n"
                        + "Your book '" + bookTitle
                        + "' is overdue.\n"
                        + "Due date: " + formatDate(dueDate)
                        + ".\n\n"
                        + "Please return the book and pay the applicable fine.";

        sendToUser(userId, message);
    }

    @Override
    public void sendFinePaidNotification(
            Long userId,
            String bookTitle,
            BigDecimal fineAmount
    ) {

        String message =
                "Fine payment successful.\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Fine paid: $" + fineAmount
                        + "\n\n"
                        + "Thank you.";

        sendToUser(userId, message);
    }

    private void sendToUser(
            Long userId,
            String message
    ) {

        System.out.println("========== TELEGRAM NOTIFICATION ==========");
        System.out.println("User ID: " + userId);

        TelegramAccount account =
                telegramAccountRepository
                        .findByUser_Id(userId)
                        .orElse(null);

        if (account == null) {
            System.out.println("Telegram account NOT FOUND");
            return;
        }

        System.out.println("Telegram account found");
        System.out.println("Chat ID: " + account.getChatId());
        System.out.println("Username: " + account.getUsername());
        System.out.println("Connected: " + account.isConnected());

        if (!account.isConnected()) {
            System.out.println("Telegram account is NOT CONNECTED");
            return;
        }

        if (account.getChatId() == null) {
            System.out.println("Chat ID is NULL");
            return;
        }

        System.out.println("Calling Telegram Bot...");

        telegramBotService.sendMessage(
                account.getChatId(),
                message
        );

        System.out.println("Telegram Bot sendMessage() finished");
        System.out.println("============================================");
    }

    private String formatDate(LocalDate date) {

        if (date == null) {
            return "N/A";
        }
        return date.format(dateFormatter);
    }
    @Override
    public void sendBorrowRejectedNotification(
            Long userId,
            String bookTitle
    ) {
        String message =
                "Your borrowing request has been rejected.\n\n"
                        + "Book: " + bookTitle;

        sendToUser(userId, message);
    }
}