package com.example.LibraryBack.dto.telegram;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramChat {
    private Long id;
    private String type;
    private String username;
    private String fullname;
}
