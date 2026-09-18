package com.example.LibraryBack.telegram;

import com.example.LibraryBack.config.TelegramConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    private final TelegramConfig telegramConfig;

    private TelegramClient telegramClient;

    private TelegramClient getTelegramClient() {

        if (telegramClient == null) {
            telegramClient =
                    new OkHttpTelegramClient(
                            telegramConfig.getBotToken()
                    );
        }

        return telegramClient;
    }

    @Override
    public void sendMessage(
            Long chatId,
            String message) {

        if (chatId == null) {
            return;
        }

        if (message == null || message.isBlank()) {
            return;
        }

        SendMessage sendMessage =
                SendMessage.builder()
                        .chatId(chatId)
                        .text(message)
                        .build();

        try {

            getTelegramClient().execute(sendMessage);

            System.out.println(
                    "Telegram message sent to chatId: "
                            + chatId
            );

        } catch (TelegramApiException e) {

            System.out.println(
                    "Failed to send Telegram message: "
                            + e.getMessage()
            );
        }
    }

    @Override
    public void sendBorrowAcceptedNotification(Long chatId, String bookTitle, LocalDate dueDate) {
        String message =
                "Your borrowing request has been accepted!\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Due date: " + dueDate;

        sendMessage(chatId, message);
    }

    @Override
    public void sendDueInTwoDaysNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate) {

        String message =
                "Your borrowed book is due in 2 days.\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Due date: " + dueDate;

        sendMessage(chatId, message);
    }

    @Override
    public void sendDueTomorrowNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate) {

        String message =
                "⚠️ Your borrowed book is due tomorrow.\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Due date: " + dueDate;

        sendMessage(chatId, message);
    }

    @Override
    public void sendOverdueNotification(
            Long chatId,
            String bookTitle,
            LocalDate dueDate) {

        String message =
                "Your borrowed book is overdue.\n\n"
                        + "Book: " + bookTitle + "\n"
                        + "Due date: " + dueDate;

        sendMessage(chatId, message);
    }

    @Override
    public void sendFinePaidNotification(
            Long chatId,
            BigDecimal amount) {

        String message =
                "Fine payment received.\n\n"
                        + "Amount paid: $"
                        + amount.setScale(2)
                        .toPlainString();

        sendMessage(chatId, message);
    }

    @Override
    public void sendBorrowRejectedNotification(
            Long chatId,
            String bookTitle) {

        String message =
                "Your borrowing request was rejected.\n\n"
                        + "Book: " + bookTitle;

        sendMessage(chatId, message);
    }
}