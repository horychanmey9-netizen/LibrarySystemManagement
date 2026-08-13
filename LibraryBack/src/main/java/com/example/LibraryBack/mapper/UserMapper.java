package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.LoginRequest;
import com.example.LibraryBack.dto.request.RegisterRequest;
import com.example.LibraryBack.dto.request.UserRequest;
import com.example.LibraryBack.dto.response.LoginResponse;
import com.example.LibraryBack.dto.response.RegisterResponse;
import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest userRequest);
    UserResponse userResponse(User user);
    User toRegister(RegisterRequest registerRequest);
    RegisterResponse registerResponse(User user);
    User toLogin(LoginRequest loginRequest);
    LoginResponse loginResponse(User user);
}
