package com.example.LibraryBack.dto.telegram;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessage {
    private Long message_id;
    private TelegramUser from;
    private TelegramChat chat;
    private Integer date;
    private String text;
}
