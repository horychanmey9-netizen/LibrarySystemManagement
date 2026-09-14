package com.example.LibraryBack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramStatusResponse {

    private boolean connected;
    private Long chatId;
    private String username;
    private LocalDateTime connectedAt;
}