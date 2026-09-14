package com.example.LibraryBack.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramBotUpdateHandler {

    private final TelegramConnectionService connectionService;
    private final TelegramBotService telegramBotService;

    public void handleUpdate(
            Long chatId,
            String username,
            String text) {

        if (text == null || text.isBlank()) {
            return;
        }

        if (text.startsWith("/start")) {

            handleStartCommand(
                    chatId,
                    username,
                    text
            );

            return;
        }

        telegramBotService.sendMessage(
                chatId,
                "Welcome to the Library System Bot!\n\n"
                        + "To connect your Telegram account, "
                        + "please use the Connect Telegram button "
                        + "in the Library System."
        );
    }

    private void handleStartCommand(
            Long chatId,
            String username,
            String text) {

        String[] parts = text.split("\\s+", 2);

        // User just pressed Start without a connection code
        if (parts.length < 2 || parts[1].isBlank()) {

            telegramBotService.sendMessage(
                    chatId,
                    "Welcome to the Library System!\n\n"
                            + "Please open your Library System account "
                            + "and click \"Connect Telegram\"."
            );

            return;
        }

        String code = parts[1].trim();

        try {
            connectionService.connectTelegram(code, chatId, username);
            telegramBotService.sendMessage(chatId,
                    "Telegram connected successfully!\n\n"
                            + "You will now receive library notifications "
                            + "through Telegram."
            );

        } catch (RuntimeException e) {
            telegramBotService.sendMessage(
                    chatId,e.getMessage()
            );
        }
    }
}