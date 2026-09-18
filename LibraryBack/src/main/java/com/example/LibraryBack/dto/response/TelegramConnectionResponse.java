package com.example.LibraryBack.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramConnectionResponse {
    private String code;
    private String botUsername;
    private String telegramLink;
    private String message;
}