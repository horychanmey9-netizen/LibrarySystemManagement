package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.ProfileResponse;
import com.example.LibraryBack.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Value("${app.upload.dir:uploads/profile}")
    private String uploadDirectory;
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

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile image

    ) throws IOException {

        return new ApiResponse<>(
                "Profile created successfully",
                HttpStatus.CREATED.value(),
                profileService.createProfile(
                        profileRequest,
                        image
                )
        );
    }

    // =========================================
    // UPDATE PROFILE
    // =========================================

    @RequestMapping(
            value = "/updateProfile",
            method = {
                    RequestMethod.PUT,
                    RequestMethod.POST
            }
    )
    public ApiResponse<ProfileResponse> updateProfile(

            @ModelAttribute ProfileRequest profileRequest,

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile image

    ) throws IOException {

        return new ApiResponse<>(
                "Profile updated successfully",
                HttpStatus.OK.value(),
                profileService.updateProfile(
                        profileRequest,
                        image
                )
        );
    }

    // =========================================
    // SERVE PROFILE IMAGE
    // =========================================

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getProfileImage(
            @PathVariable String filename
    ) throws IOException {

        Path filePath =
                Paths.get(uploadDirectory)
                        .resolve(filename)
                        .normalize();

        /*
         * Prevent accessing files outside upload directory.
         */
        Path uploadPath =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();

        Path requestedPath =
                filePath.toAbsolutePath()
                        .normalize();

        if (!requestedPath.startsWith(uploadPath)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        if (!Files.exists(requestedPath)
                || !Files.isRegularFile(requestedPath)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Resource resource =
                new UrlResource(
                        requestedPath.toUri()
                );

        String contentType =
                Files.probeContentType(requestedPath);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                contentType
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                requestedPath.getFileName() +
                                "\""
                )
                .body(resource);
    }
}