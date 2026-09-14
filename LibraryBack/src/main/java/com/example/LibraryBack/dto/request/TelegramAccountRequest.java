package com.example.LibraryBack.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramAccountRequest {
    @NotNull
    private Long chatId;
    private Long telegramUserId;
    private String username;
    private String fullname;
    private Long userId;
}