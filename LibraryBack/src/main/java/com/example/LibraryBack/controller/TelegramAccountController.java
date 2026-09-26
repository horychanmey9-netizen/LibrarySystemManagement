package com.example.LibraryBack.controller;

import com.example.LibraryBack.config.TelegramConfig;
import com.example.LibraryBack.dto.response.TelegramConnectionResponse;
import com.example.LibraryBack.dto.response.TelegramStatusResponse;
import com.example.LibraryBack.dto.request.SendMessageRequest;
import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import com.example.LibraryBack.repository.UserRepository;
import com.example.LibraryBack.service.TelegramMessageService;
import com.example.LibraryBack.telegram.TelegramConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramAccountController {

    private final TelegramConnectionService connectionService;
    private final TelegramAccountRepository telegramAccountRepository;
    private final UserRepository userRepository;
    private final TelegramConfig telegramConfig;
    private final TelegramMessageService telegramMessageService;

    @PostMapping("/connect")
    public TelegramConnectionResponse generateConnectionCode(Authentication authentication) {
        User user = getCurrentUser(authentication);
        String connectionCode = connectionService.generateConnectionCode(user.getId());
        String botUsername = telegramConfig.getBotUsername();
        String telegramLink = "https://t.me/" + botUsername + "?start=" + connectionCode;
        return TelegramConnectionResponse.builder()
                .code(connectionCode)
                .botUsername(botUsername)
                .telegramLink(telegramLink)
                .message("Open Telegram and click the link to connect your account.")
                .build();
    }

    @GetMapping("/status")
    public TelegramStatusResponse getTelegramStatus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return new TelegramStatusResponse(false, null, null, null);
        }
        User user = getCurrentUser(authentication);
        TelegramAccount account = telegramAccountRepository.findByUser_Id(user.getId()).orElse(null);
        if (account == null) {
            return new TelegramStatusResponse(false, null, null, null);
        }
        return new TelegramStatusResponse(account.isConnected(), account.getChatId(), account.getUsername(), account.getConnectedAt());
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<String> disconnectTelegram(Authentication authentication) {
        User user = getCurrentUser(authentication);
        connectionService.disconnectTelegram(user.getId());
        return ResponseEntity.ok("Telegram account disconnected successfully");
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        telegramMessageService.sendMessage(user.getId(), request.getMessage());
        return ResponseEntity.ok("Message sent successfully");
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
