package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.UserRequest;
import com.example.LibraryBack.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUser();
    public UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
}
