package com.example.LibraryBack.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for sending a message to the user's connected Telegram account.
 */
@Data
public class SendMessageRequest {
    @NotBlank(message = "Message cannot be empty")
    private String message;
}
