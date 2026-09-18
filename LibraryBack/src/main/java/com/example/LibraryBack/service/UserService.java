package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getData();
//    void deleteUser(Long id);
void deactivateUser(Long id);
    void activateUser(Long id);
}
