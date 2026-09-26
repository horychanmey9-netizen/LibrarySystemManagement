package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.SendMessageRequest;
import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import com.example.LibraryBack.config.TelegramConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class TelegramMessageServiceImpl implements TelegramMessageService {

    private final TelegramAccountRepository accountRepository;
    private final TelegramConfig telegramConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramMessageServiceImpl(TelegramAccountRepository accountRepository, TelegramConfig telegramConfig) {
        this.accountRepository = accountRepository;
        this.telegramConfig = telegramConfig;
    }

    @Override
    public void sendMessage(Long userId, String message) {
        // Find telegram account linked to user
        TelegramAccount account = accountRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Telegram account not connected"));

        if (!account.isConnected() || account.getChatId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not connected to Telegram");
        }

        String token = telegramConfig.getBotToken();
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";

        Map<String, Object> payload = Map.of(
                "chat_id", account.getChatId(),
                "text", message
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send Telegram message", ex);
        }
    }
}

