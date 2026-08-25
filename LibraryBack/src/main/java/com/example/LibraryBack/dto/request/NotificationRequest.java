package com.example.LibraryBack.dto.request;

import com.example.LibraryBack.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    @NotNull
    private Long userId;
    private String title;
    private String message;
    @NotNull
    private NotificationType type;
    private Long referenceId;
}
