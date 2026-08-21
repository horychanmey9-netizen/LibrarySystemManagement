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
import org.springframework.data.crossstore.ChangeSetPersister;
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
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotException("User not found"));
    }

    @Override
    public ProfileResponse getProfile() {
        User user = getCurrentUser();
        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotException("Profile not found"));
        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest, MultipartFile image) throws IOException {
        User user = getCurrentUser();
        if (profileRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Profile already exists");
        }
        Profile profile = profileMapper.toEntity(profileRequest);
        profile.setUser(user);
        String fileName = image.getOriginalFilename();
        String fileUrl = UUID.randomUUID() + "_" + fileName;
        Path path = Paths.get("uploads/profile");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Files.copy(image.getInputStream(), path.resolve(fileUrl));
        String imageUrl = "http://localhost:8080/uploads/profile/" + fileUrl;
        profile.setImage(imageUrl);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(savedProfile);
    }

    @Override
    public ProfileResponse updateProfile(ProfileRequest profileRequest, MultipartFile image) throws IOException {
        User user = getCurrentUser();
        Profile profile = profileRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new NotException("Profile not found"));
        profile.setPhone(profileRequest.getPhone());
        profile.setGender(Gender.valueOf(profileRequest.getGender()));
        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        profile.setAddress(profileRequest.getAddress());
        String fileName = image.getOriginalFilename();
        String fileUrl = UUID.randomUUID() + "_" + fileName;
        Path path = Paths.get("uploads/profile");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        Files.copy(image.getInputStream(), path.resolve(fileUrl));
        String imageUrl = "http://localhost:8080/uploads/profile/" + fileUrl;
        profile.setImage(imageUrl);
        Profile updatedProfile =
                profileRepository.save(profile);
        return profileMapper.toResponse(updatedProfile);
    }
}
