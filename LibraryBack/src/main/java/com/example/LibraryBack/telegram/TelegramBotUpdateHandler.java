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
            String text
    ) {

        if (chatId == null || text == null) {
            return;
        }

        System.out.println("========== TELEGRAM MESSAGE ==========");
        System.out.println("Chat ID: " + chatId);
        System.out.println("Username: " + username);
        System.out.println("Text: " + text);
        System.out.println("======================================");

        /*
         * /start
         */
        if (text.equals("/start")) {

            telegramBotService.sendMessage(
                    chatId,
                    "Welcome to My Library System Bot!\n\n"
                            + "Please generate a connection code "
                            + "from the Library System and send it here."
            );

            return;
        }

        /*
         * /start CODE
         */
        if (text.startsWith("/start ")) {

            String code = text.substring(7).trim();

            if (code.isBlank()) {

                telegramBotService.sendMessage(
                        chatId,
                        "Please provide a valid connection code."
                );

                return;
            }

            try {

                connectionService.connectTelegram(
                        code,
                        chatId,
                        username
                );

                telegramBotService.sendMessage(
                        chatId,
                        "Your Telegram account has been connected "
                                + "successfully to your Library account."
                );

            } catch (Exception e) {

                System.out.println(
                        "Telegram connection failed: "
                                + e.getMessage()
                );

                telegramBotService.sendMessage(
                        chatId,
                        "Connection failed. "
                                + "Please check your connection code "
                                + "and try again."
                );
            }

            return;
        }

        /*
         * Unknown command
         */
        telegramBotService.sendMessage(
                chatId,
                "I don't understand that command.\n\n"
                        + "Please use /start."
        );
    }
}