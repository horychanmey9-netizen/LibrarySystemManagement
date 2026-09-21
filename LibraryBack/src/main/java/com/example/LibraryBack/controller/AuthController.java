package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.*;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.LoginResponse;
import com.example.LibraryBack.dto.response.RegisterResponse;
import com.example.LibraryBack.dto.response.VerifyOtpResponse;
import com.example.LibraryBack.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return new ApiResponse<>(
                "register successfully",
                201,
                authService.register(registerRequest)
        );
    }


    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ApiResponse<>(
                "login successfully",
                200,
                authService.login(loginRequest)
        );
    }


    @PostMapping("/verify-otp")
    public ApiResponse<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return new ApiResponse<>(
                "OTP verified successfully",
                200,
                authService.verifyOtp(request)
        );
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return new ApiResponse<>(
                "Password reset email sent successfully",
                200,
                authService.forgotPassword(request)
        );
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ApiResponse<>(
                "Password has been changed successfully",
                200,
                authService.resetPassword(request)
        );
    }
    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(
            @Valid
            @RequestBody
            ChangePasswordRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        return new ApiResponse<>(
                "Password changed successfully",
                200,
                authService.changePassword(
                        email,
                        request
                )
        );
    }
}
