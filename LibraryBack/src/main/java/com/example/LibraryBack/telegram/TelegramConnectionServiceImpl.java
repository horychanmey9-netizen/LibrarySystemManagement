    package com.example.LibraryBack.telegram;

    import com.example.LibraryBack.entity.TelegramAccount;
    import com.example.LibraryBack.entity.TelegramConnectionCode;
    import com.example.LibraryBack.entity.User;
    import com.example.LibraryBack.repository.TelegramAccountRepository;
    import com.example.LibraryBack.repository.TelegramConnectionCodeRepository;
    import com.example.LibraryBack.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class TelegramConnectionServiceImpl
            implements TelegramConnectionService {

        private final UserRepository userRepository;
        private final TelegramAccountRepository telegramAccountRepository;
        private final TelegramConnectionCodeRepository connectionCodeRepository;

        @Override
        @Transactional
        public String generateConnectionCode(Long userId) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            // Delete old connection code
            connectionCodeRepository.deleteByUser_Id(userId);

            // Make sure DELETE is executed before INSERT
            connectionCodeRepository.flush();

            String code = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();

            TelegramConnectionCode connectionCode =
                    new TelegramConnectionCode();

            connectionCode.setUser(user);
            connectionCode.setCode(code);
            connectionCode.setExpiryDate(
                    LocalDateTime.now().plusMinutes(10)
            );

            connectionCodeRepository.save(connectionCode);

            return code;
        }

        @Override
        @Transactional
        public void connectTelegram(
                String code,
                Long chatId,
                String username) {

            TelegramConnectionCode connectionCode =
                    connectionCodeRepository
                            .findByCode(code)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Invalid connection code"
                                    ));

            if (connectionCode.getExpiryDate()
                    .isBefore(LocalDateTime.now())) {

                connectionCodeRepository.delete(connectionCode);

                throw new RuntimeException(
                        "Connection code has expired"
                );
            }

            User user = connectionCode.getUser();

            /*
             * Check whether this Telegram chat is already
             * connected to another account.
             */
            telegramAccountRepository
                    .findByChatId(chatId)
                    .ifPresent(existingAccount -> {

                        if (!existingAccount
                                .getUser()
                                .getId()
                                .equals(user.getId())) {

                            throw new RuntimeException(
                                    "This Telegram account "
                                            + "is already connected "
                                            + "to another user"
                            );
                        }
                    });

            /*
             * Check whether this library user already
             * has a Telegram account.
             */
            TelegramAccount account =
                    telegramAccountRepository
                            .findByUser_Id(user.getId())
                            .orElseGet(() -> TelegramAccount.builder()
                                    .active(true)
                                    .connected(false)
                                    .build());

            account.setUser(user);
            account.setChatId(chatId);
            account.setUsername(username);
            account.setConnected(true);
            account.setActive(true);
            account.setConnectedAt(LocalDateTime.now());

            telegramAccountRepository.save(account);

            /*
             * Code can only be used once.
             */
            connectionCodeRepository.delete(connectionCode);
        }

        @Override
        @Transactional
        public void disconnectTelegram(Long userId) {

            telegramAccountRepository
                    .findByUser_Id(userId)
                    .ifPresent(account -> {

                        account.setConnected(false);

                        telegramAccountRepository.save(account);
                    });
        }
    }