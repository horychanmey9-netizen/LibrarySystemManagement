package com.example.LibraryBack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_accounts", uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_telegram_chat_id",
                        columnNames = "chat_id")})
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
    private LocalDateTime connectedAt;

    public LocalDateTime getConnectedAt() {
        return this.connectedAt;
    }
}