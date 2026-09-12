package com.example.LibraryBack.telegram;

import com.example.LibraryBack.dto.request.TelegramAccountRequest;
import com.example.LibraryBack.dto.response.TelegramAccountResponse;

import java.util.List;

public interface TelegramAccountService {
    TelegramAccountResponse createTelegramAccount(TelegramAccountRequest request);
    TelegramAccountResponse getTelegramAccountById(Long id);
    TelegramAccountResponse getTelegramAccountByUserId(Long userId);
    List<TelegramAccountResponse> getAllTelegramAccounts();
    TelegramAccountResponse updateTelegramAccount(Long id, TelegramAccountRequest request);
    void deleteTelegramAccount(Long id);
}