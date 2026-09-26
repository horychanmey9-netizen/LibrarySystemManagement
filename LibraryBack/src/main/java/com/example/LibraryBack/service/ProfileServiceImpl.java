package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ProfileResponse;
import com.example.LibraryBack.entity.Profile;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.Gender;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.ProfileMapper;
import com.example.LibraryBack.repository.ProfileRepository;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${app.backend.url:http://localhost:8080}")
    private String backendUrl;

    @Value("${app.upload.dir:uploads/profile}")
    private String uploadDirectory;

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

    private Gender parseGender(String genderStr) {

        if (genderStr == null || genderStr.trim().isEmpty()) {
            return null;
        }

        String clean = genderStr.trim();

        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(clean)) {
                return gender;
            }
        }

        return null;
    }

    private void saveProfileImage(
            Profile profile,
            MultipartFile image
    ) throws IOException {

        if (image == null || image.isEmpty()) {
            return;
        }

        String originalFileName = image.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "profile-image";
        }

        /*
         * Remove unsafe characters from original filename.
         */
        originalFileName = Paths
                .get(originalFileName)
                .getFileName()
                .toString()
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;

        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        /*
         * Save image to server filesystem.
         */
        Files.copy(
                image.getInputStream(),
                filePath
        );

        /*
         * Save the public URL into PostgreSQL.
         */
        String cleanBackendUrl =
                backendUrl.endsWith("/")
                        ? backendUrl.substring(
                        0,
                        backendUrl.length() - 1
                )
                        : backendUrl;

        String imageUrl =
                cleanBackendUrl
                        + "/uploads/profile/"
                        + fileName;

        profile.setImage(imageUrl);
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
                        .orElse(null);

        if (profile == null) {
            return ProfileResponse.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }

        return profileMapper.toResponse(profile);
    }

    // =========================================
    // CREATE PROFILE
    // =========================================
    @Override
    @Transactional
    public ProfileResponse createProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {

        return saveOrUpdateProfile(
                profileRequest,
                image
        );
    }

    // =========================================
    // UPDATE PROFILE
    // =========================================
    @Override
    @Transactional
    public ProfileResponse updateProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {

        return saveOrUpdateProfile(
                profileRequest,
                image
        );
    }
    private ProfileResponse saveOrUpdateProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {
        User user = getCurrentUser();

        Profile profile =
                profileRepository
                        .findByUserId(user.getId())
                        .orElseGet(() -> {

                            Profile newProfile =
                                    new Profile();

                            newProfile.setUser(user);

                            return newProfile;
                        });

        // =========================================
        // NAME
        // =========================================

        if (profileRequest.getName() != null
                && !profileRequest.getName().trim().isEmpty()) {

            String updatedName =
                    profileRequest
                            .getName()
                            .trim();

            profile.setName(updatedName);

            user.setName(updatedName);

            userRepository.save(user);
        }

        // =========================================
        // OTHER PROFILE INFORMATION
        // =========================================

        profile.setPhone(
                profileRequest.getPhone()
        );

        profile.setGender(
                parseGender(
                        profileRequest.getGender()
                )
        );

        profile.setDateOfBirth(
                profileRequest.getDateOfBirth()
        );

        profile.setAddress(
                profileRequest.getAddress()
        );

        // =========================================
        // PROFILE IMAGE
        // =========================================

        if (image != null && !image.isEmpty()) {

            saveProfileImage(
                    profile,
                    image
            );
        }
        Profile savedProfile =
                profileRepository.save(profile);

        return profileMapper.toResponse(
                savedProfile
        );
    }
}