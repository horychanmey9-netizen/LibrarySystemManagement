package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.UserRequest;
import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/GetUser")
    public ResponseEntity<List<UserResponse>> getUser() {

        List<UserResponse> users = userService.getUser();

        return ResponseEntity.ok(users);
    }
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest) {

        UserResponse response = userService.updateUser(id, userRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}


