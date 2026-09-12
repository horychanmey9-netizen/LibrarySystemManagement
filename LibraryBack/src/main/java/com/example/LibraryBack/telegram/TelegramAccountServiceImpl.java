package com.example.LibraryBack.telegram;

import com.example.LibraryBack.dto.request.TelegramAccountRequest;
import com.example.LibraryBack.dto.response.TelegramAccountResponse;
import com.example.LibraryBack.entity.TelegramAccount;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.mapper.TelegramAccountMapper;
import com.example.LibraryBack.repository.TelegramAccountRepository;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TelegramAccountServiceImpl implements TelegramAccountService {

    private final TelegramAccountRepository telegramAccountRepository;
    private final TelegramAccountMapper telegramAccountMapper;
    private final UserRepository userRepository;

    @Override
    public TelegramAccountResponse createTelegramAccount(
            TelegramAccountRequest request
    ) {
        if (telegramAccountRepository.existsByChatId(request.getChatId())) {
            throw new RuntimeException(
                    "Telegram account with this chat ID already exists"
            );
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + request.getUserId()
                        )
                );
        if (telegramAccountRepository.existsByUser_Id(request.getUserId())) {
            throw new RuntimeException(
                    "This user already has a Telegram account"
            );
        }
        TelegramAccount telegramAccount =
                telegramAccountMapper.toEntity(request);
        telegramAccount.setUser(user);

        TelegramAccount saved = telegramAccountRepository.save(telegramAccount);
        return telegramAccountMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramAccountResponse getTelegramAccountById(Long id) {

        TelegramAccount telegramAccount =
                telegramAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Telegram account not found with ID: " + id
                                )
                        );

        return telegramAccountMapper.toResponse(telegramAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramAccountResponse getTelegramAccountByUserId(
            Long userId
    ) {

        TelegramAccount telegramAccount =
                telegramAccountRepository.findByUser_Id(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Telegram account not found for user ID: "
                                                + userId
                                )
                        );

        return telegramAccountMapper.toResponse(telegramAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelegramAccountResponse> getAllTelegramAccounts() {

        return telegramAccountRepository.findAll()
                .stream()
                .map(telegramAccountMapper::toResponse)
                .toList();
    }

    @Override
    public TelegramAccountResponse updateTelegramAccount(
            Long id,
            TelegramAccountRequest request
    ) {

        TelegramAccount telegramAccount =
                telegramAccountRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Telegram account not found with ID: " + id
                                )
                        );

        // Check if new chat ID belongs to another Telegram account
        telegramAccountRepository.findByChatId(request.getChatId())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException(
                                "Telegram chat ID is already used by another account"
                        );
                    }
                });

        // Find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + request.getUserId()
                        )
                );

        // Update fields
        telegramAccount.setChatId(request.getChatId());
        telegramAccount.setUser(user);

        TelegramAccount updated =
                telegramAccountRepository.save(telegramAccount);

        return telegramAccountMapper.toResponse(updated);
    }

    @Override
    public void deleteTelegramAccount(Long id) {

        if (!telegramAccountRepository.existsById(id)) {
            throw new RuntimeException(
                    "Telegram account not found with ID: " + id
            );
        }

        telegramAccountRepository.deleteById(id);
    }
}