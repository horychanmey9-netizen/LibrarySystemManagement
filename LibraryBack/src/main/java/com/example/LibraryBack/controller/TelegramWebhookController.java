package com.example.LibraryBack.controller;

import com.example.LibraryBack.telegram.TelegramBotUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramBotUpdateHandler updateHandler;

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveUpdate(
            @RequestBody JsonNode update
    ) {

        System.out.println("========== TELEGRAM WEBHOOK ==========");

        JsonNode message = update.get("message");

        if (message == null) {
            return ResponseEntity.ok().build();
        }

        JsonNode from = message.get("from");
        JsonNode chat = message.get("chat");
        JsonNode text = message.get("text");

        if (chat == null || text == null) {
            return ResponseEntity.ok().build();
        }

        Long chatId = chat.path("id").asLong();

        String username = null;

        if (from != null) {
            username = from.path("username").asText(null);
        }

        String messageText = text.asText();

        System.out.println("Telegram User ID: "
                + (from != null ? from.path("id").asLong() : null));

        System.out.println("Username: " + username);
        System.out.println("Chat ID: " + chatId);
        System.out.println("Message: " + messageText);

        System.out.println("======================================");

        updateHandler.handleUpdate(
                chatId,
                username,
                messageText
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> testWebhook() {
        return ResponseEntity.ok(
                "Telegram webhook endpoint is working"
        );
    }
}