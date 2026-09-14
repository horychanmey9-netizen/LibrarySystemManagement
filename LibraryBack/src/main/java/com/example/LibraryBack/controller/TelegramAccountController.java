package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.response.TelegramConnectionResponse;
import com.example.LibraryBack.dto.response.TelegramStatusResponse;
import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import com.example.LibraryBack.repository.UserRepository;
import com.example.LibraryBack.telegram.TelegramConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramAccountController {

    private final TelegramConnectionService connectionService;
    private final TelegramAccountRepository telegramAccountRepository;
    private final UserRepository userRepository;

    @PostMapping("/connect")
    public TelegramConnectionResponse generateConnectionCode(
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        return connectionService.generateConnectionCode(
                user.getId()
        );
    }

    @GetMapping("/status")
    public TelegramStatusResponse getTelegramStatus(
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        TelegramAccount account =
                telegramAccountRepository
                        .findByUser_Id(user.getId())
                        .orElse(null);

        if (account == null) {
            return new TelegramStatusResponse(
                    false,
                    null,
                    null,
                    null
            );
        }

        return new TelegramStatusResponse(
                true,
                account.getChatId(),
                account.getUsername(),
                account.getConnectedAt()
        );
    }
    @DeleteMapping("/disconnect")
    public ResponseEntity<String> disconnectTelegram(
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        connectionService.disconnectTelegram(
                user.getId()
        );

        return ResponseEntity.ok(
                "Telegram account disconnected successfully"
        );
    }

    private User getCurrentUser(
            Authentication authentication) {

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));
    }
}