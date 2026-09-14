package com.example.LibraryBack.telegram;

import com.example.LibraryBack.dto.response.TelegramConnectionResponse;
import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.entity.TelegramConnectionCode;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import com.example.LibraryBack.repository.TelegramConnectionCodeRepository;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TelegramConnectionServiceImpl
        implements TelegramConnectionService {

    private final TelegramConnectionCodeRepository connectionCodeRepository;
    private final TelegramAccountRepository telegramAccountRepository;
    private final UserRepository userRepository;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public TelegramConnectionResponse generateConnectionCode(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        connectionCodeRepository.deleteByUserId(userId);

        String code = generateCode();

        TelegramConnectionCode connectionCode =
                TelegramConnectionCode.builder()
                        .code(code)
                        .user(user)
                        .expiresAt(
                                LocalDateTime.now().plusMinutes(5)
                        )
                        .used(false)
                        .build();

        connectionCodeRepository.save(connectionCode);

        String telegramLink =
                "https://t.me/"
                        + botUsername
                        + "?start="
                        + code;

        return TelegramConnectionResponse.builder()
                .code(code)
                .botUsername(botUsername)
                .telegramLink(telegramLink)
                .message(
                        "Open Telegram and press Start to connect your account."
                )
                .build();
    }

    @Override
    @Transactional
    public void connectTelegram(
            String code,
            Long chatId,
            String username) {

        System.out.println("========== TELEGRAM CONNECT ==========");
        System.out.println("Code: " + code);
        System.out.println("Chat ID: " + chatId);
        System.out.println("Username: " + username);

        TelegramConnectionCode connectionCode =
                connectionCodeRepository
                        .findByCode(code)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid connection code"
                                ));

        System.out.println("Connection code found!");
        System.out.println("User ID: " +
                connectionCode.getUser().getId());

        if (connectionCode.isUsed()) {
            throw new RuntimeException(
                    "This connection code has already been used"
            );
        }

        if (connectionCode.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "This connection code has expired"
            );
        }

        User user = connectionCode.getUser();

        System.out.println("User found: " + user.getId());

        telegramAccountRepository
                .findByChatId(chatId)
                .ifPresent(existingAccount -> {

                    if (!existingAccount.getUser()
                            .getId()
                            .equals(user.getId())) {

                        throw new RuntimeException(
                                "This Telegram account is already connected "
                                        + "to another library account"
                        );
                    }
                });

        TelegramAccount telegramAccount =
                telegramAccountRepository
                        .findByUser_Id(user.getId())
                        .orElse(
                                TelegramAccount.builder()
                                        .user(user)
                                        .build()
                        );

        System.out.println("Creating/updating TelegramAccount...");

        telegramAccount.setChatId(chatId);
        telegramAccount.setUsername(username);
        telegramAccount.setConnected(true);
        telegramAccount.setConnectedAt(LocalDateTime.now());

        telegramAccountRepository.save(telegramAccount);

        System.out.println("TelegramAccount SAVED!");
        System.out.println("Account ID: " + telegramAccount.getId());

        connectionCode.setUsed(true);

        connectionCodeRepository.save(connectionCode);

        connectionCodeRepository.delete(connectionCode);

        System.out.println("========== CONNECT SUCCESS ==========");
    }

    @Override
    @Transactional
    public void disconnectTelegram(Long userId) {
        TelegramAccount telegramAccount = telegramAccountRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Telegram account not found"));
        telegramAccount.setConnected(false);
        telegramAccount.setChatId(null);
        telegramAccount.setUsername(null);

        telegramAccountRepository.save(telegramAccount);
    }

    private String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = secureRandom.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}