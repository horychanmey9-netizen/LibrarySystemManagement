package com.example.LibraryBack.dto.request;

import com.example.LibraryBack.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserRequest {
    private String name;
    private String email;
    private Role role;
    private String phone;
}
