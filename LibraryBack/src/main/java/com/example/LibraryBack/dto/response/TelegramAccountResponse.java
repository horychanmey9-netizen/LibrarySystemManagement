package com.example.LibraryBack.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramAccountResponse {
    private Long id;
    private Long chatId;
    private Long telegramUserId;
    private String username;
    private String fullName;
    private Boolean active;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}