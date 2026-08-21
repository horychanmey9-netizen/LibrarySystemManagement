package com.example.LibraryBack.dto.response;

import com.example.LibraryBack.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
