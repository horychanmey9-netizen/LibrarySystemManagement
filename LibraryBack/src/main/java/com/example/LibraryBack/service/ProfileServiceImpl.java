package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ProfileResponse;
import com.example.LibraryBack.entity.Profile;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.Gender;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.ProfileMapper;
import com.example.LibraryBack.repositoy.ProfileRepository;
import com.example.LibraryBack.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NotException("User not found")
                );
    }


    // =========================================
    // GET PROFILE
    // =========================================
    @Override
    public ProfileResponse getProfile() {

        User user = getCurrentUser();

        Profile profile =
                profileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(
                                () -> new NotException(
                                        "Profile not found"
                                )
                        );

        return profileMapper.toResponse(profile);
    }


    // =========================================
    // CREATE PROFILE
    // =========================================
    @Override
    public ProfileResponse createProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {

        User user = getCurrentUser();

        if (profileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException(
                    "Profile already exists"
            );
        }

        Profile profile =
                profileMapper.toEntity(profileRequest);

        profile.setUser(user);


        // ==============================
        // SAVE IMAGE
        // ==============================
        if (image != null && !image.isEmpty()) {

            String originalFileName =
                    image.getOriginalFilename();

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;

            Path uploadPath =
                    Paths.get("uploads/profile");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    image.getInputStream(),
                    filePath
            );

            String imageUrl =
                    "http://localhost:8080/uploads/profile/"
                            + fileName;

            profile.setImage(imageUrl);
        }


        Profile savedProfile =
                profileRepository.save(profile);

        return profileMapper.toResponse(savedProfile);
    }


    // =========================================
    // UPDATE PROFILE
    // =========================================
    @Override
    public ProfileResponse updateProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {

        User user = getCurrentUser();

        Profile profile =
                profileRepository
                        .findByUserId(user.getId())
                        .orElseThrow(
                                () -> new NotException(
                                        "Profile not found"
                                )
                        );


        // ==============================
        // UPDATE PROFILE DATA
        // ==============================

        profile.setPhone(
                profileRequest.getPhone()
        );

        profile.setGender(
                Gender.valueOf(
                        profileRequest.getGender()
                )
        );

        profile.setDateOfBirth(
                profileRequest.getDateOfBirth()
        );

        profile.setAddress(
                profileRequest.getAddress()
        );


        // ==============================
        // UPDATE IMAGE ONLY IF NEW IMAGE
        // ==============================

        if (image != null && !image.isEmpty()) {

            String originalFileName =
                    image.getOriginalFilename();

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + originalFileName;

            Path uploadPath =
                    Paths.get("uploads/profile");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.copy(
                    image.getInputStream(),
                    filePath
            );

            String imageUrl =
                    "http://localhost:8080/uploads/profile/"
                            + fileName;

            profile.setImage(imageUrl);
        }


        // ==============================
        // SAVE DATABASE
        // ==============================

        Profile updatedProfile =
                profileRepository.save(profile);

        return profileMapper.toResponse(
                updatedProfile
        );
    }
}