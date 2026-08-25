package com.example.LibraryBack.dto.response;

import com.example.LibraryBack.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private Boolean isRead;
    private Boolean isStarred;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Long referenceId;
}
