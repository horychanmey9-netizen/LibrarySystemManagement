package com.example.LibraryBack.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestClient restClient = RestClient.builder().build();

    @Override
    public void sendMessage(Long chatId, String message) {

        String url = "https://api.telegram.org/bot"
                + botToken
                + "/sendMessage";

        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "chat_id", chatId,
                        "text", message
                ))
                .retrieve()
                .toBodilessEntity();
    }
}