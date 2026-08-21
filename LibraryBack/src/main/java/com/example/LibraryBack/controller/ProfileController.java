package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.ProfileResponse;
import com.example.LibraryBack.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/getProfile")
    public ApiResponse<ProfileResponse> getProfile() {
        return new ApiResponse<>(
                "Profile retrieved successfully",
                HttpStatus.OK.value(),
                profileService.getProfile()
        );
    }

    @PostMapping("/createProfile")
    public ApiResponse<ProfileResponse> createProfile(
            @ModelAttribute ProfileRequest profileRequest,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        return new ApiResponse<>(
                "Profile created successfully",
                HttpStatus.CREATED.value(),
                profileService.createProfile(profileRequest, image)
        );
    }

    @PutMapping("/updateProfile")
    public ApiResponse<ProfileResponse> updateProfile(
            @ModelAttribute ProfileRequest profileRequest,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        return new ApiResponse<>(
                "Profile updated successfully",
                HttpStatus.OK.value(),
                profileService.updateProfile(profileRequest, image)
        );
    }
}