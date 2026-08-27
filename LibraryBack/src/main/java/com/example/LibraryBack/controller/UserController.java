package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getData")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getData(){
        return new ApiResponse<>("Get users sucessfully",200,userService.getData());
    }

}
