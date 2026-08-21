package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ProfileResponse getProfile();
    ProfileResponse createProfile(ProfileRequest request, MultipartFile image)
            throws IOException;
    ProfileResponse updateProfile(ProfileRequest request, MultipartFile image)
            throws IOException;
}
