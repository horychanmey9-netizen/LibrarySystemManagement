package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.telegram.TelegramMessage;
import com.example.LibraryBack.dto.telegram.TelegramUpdate;
import com.example.LibraryBack.telegram.TelegramBotUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramBotUpdateHandler updateHandler;

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveUpdate(
            @RequestBody TelegramUpdate update) {

        if (update == null || update.getMessage() == null) {
            return ResponseEntity.ok().build();
        }

        TelegramMessage message = update.getMessage();

        if (message.getChat() == null) {
            return ResponseEntity.ok().build();
        }

        Long chatId = message.getChat().getId();

        String username = null;

        if (message.getFrom() != null) {
            username = message.getFrom().getUsername();
        }

        String text = message.getText();

        System.out.println("===== TELEGRAM WEBHOOK =====");
        System.out.println("Chat ID: " + chatId);
        System.out.println("Username: " + username);
        System.out.println("Text: " + text);

        updateHandler.handleUpdate(
                chatId,
                username,
                text
        );

        return ResponseEntity.ok().build();
    }
}