package com.example.LibraryBack.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {
    private Long id;
    @JsonProperty("is_bot")
    private boolean bot;
    @JsonProperty("full_name")
    private String fullname;
    private String username;
}