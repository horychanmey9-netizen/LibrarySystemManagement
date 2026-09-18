package com.example.LibraryBack.dto.telegram;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUpdate {
    private Long update_id;
    private TelegramMessage message;
}