package com.example.LibraryBack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "telegram_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_telegram_chat_id",
                        columnNames = "chat_id"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Builder.Default
    @Column(nullable = false)
    private boolean connected = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}