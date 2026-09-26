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
        for (Gender g : Gender.values()) {
            if (g.name().equalsIgnoreCase(clean)) {
                return g;
            }
        }
        return null;
    }

    private void saveProfileImage(Profile profile, MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        Path uploadPath = Paths.get("uploads/profile");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        String imageUrl = "http://localhost:8080/uploads/profile/" + fileName;
        profile.setImage(imageUrl);
    }

    // =========================================
    // GET PROFILE
    // =========================================
    @Override
    public ProfileResponse getProfile() {
        User user = getCurrentUser();

        Profile profile = profileRepository
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
        return saveOrUpdateProfile(profileRequest, image);
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
        return saveOrUpdateProfile(profileRequest, image);
    }

    private ProfileResponse saveOrUpdateProfile(
            ProfileRequest profileRequest,
            MultipartFile image
    ) throws IOException {
        User user = getCurrentUser();

        Profile profile = profileRepository
                .findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUser(user);
                    return p;
                });

        // Update user name if provided
        if (profileRequest.getName() != null && !profileRequest.getName().trim().isEmpty()) {
            String updatedName = profileRequest.getName().trim();
            profile.setName(updatedName);
            user.setName(updatedName);
            userRepository.save(user);
        }

        profile.setPhone(profileRequest.getPhone());
        profile.setGender(parseGender(profileRequest.getGender()));
        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        profile.setAddress(profileRequest.getAddress());

        if (image != null && !image.isEmpty()) {
            saveProfileImage(profile, image);
        }

        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(savedProfile);
    }
}