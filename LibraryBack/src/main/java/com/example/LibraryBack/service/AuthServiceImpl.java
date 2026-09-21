package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.ChangePasswordRequest;
import com.example.LibraryBack.dto.request.ForgotPasswordRequest;
import com.example.LibraryBack.dto.request.LoginRequest;
import com.example.LibraryBack.dto.request.RegisterRequest;
import com.example.LibraryBack.dto.request.ResetPasswordRequest;
import com.example.LibraryBack.dto.request.VerifyOtpRequest;

import com.example.LibraryBack.dto.response.LoginResponse;
import com.example.LibraryBack.dto.response.RegisterResponse;
import com.example.LibraryBack.dto.response.VerifyOtpResponse;

import com.example.LibraryBack.entity.Otp;
import com.example.LibraryBack.entity.PasswordResetToken;
import com.example.LibraryBack.entity.User;

import com.example.LibraryBack.enums.Role;

import com.example.LibraryBack.exception.EmailAlreadyExists;
import com.example.LibraryBack.exception.EmailAndPasswordAreNotMatch;
import com.example.LibraryBack.exception.NotException;

import com.example.LibraryBack.mapper.UserMapper;

import com.example.LibraryBack.repository.OtpRepository;
import com.example.LibraryBack.repository.PasswordResetTokenRepository;
import com.example.LibraryBack.repository.UserRepository;

import com.example.LibraryBack.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final OtpService otpService;

    private final OtpRepository otpRepository;

    private final UserMapper userMapper;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    /* =========================================================
       REGISTER
    ========================================================= */

    @Override
    public RegisterResponse register(
            RegisterRequest registerRequest
    ) {

        // Check email already exists
        if (userRepository
                .findByEmail(registerRequest.getEmail())
                .isPresent()) {

            throw new EmailAlreadyExists(
                    "Email already exists"
            );
        }


        // Convert request -> User
        User user =
                userMapper.toRegister(
                        registerRequest
                );


        // Encode password
        user.setPassword(
                passwordEncoder.encode(
                        registerRequest.getPassword()
                )
        );


        // Default role
        user.setRole(Role.USER);


        /*
         * New account is inactive.
         *
         * User must verify OTP first.
         */
        user.setActive(false);


        // Save user
        user =
                userRepository.save(user);


        // Create OTP
        otpService.createOtp(user);


        return userMapper.registerResponse(user);
    }


    /* =========================================================
       VERIFY OTP
    ========================================================= */

    @Override
    public VerifyOtpResponse verifyOtp(
            VerifyOtpRequest request
    ) {

        // Find user by email
        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new NotException(
                                        "Email not found"
                                )
                        );


        // Find OTP
        Otp otp =
                otpRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP not found"
                                )
                        );


        // Check OTP expiry
        if (otp.getExpiryTime()
                .isBefore(
                        LocalDateTime.now()
                )) {

            throw new RuntimeException(
                    "OTP has expired"
            );
        }


        // Check OTP
        if (!otp.getOtp()
                .equals(
                        request.getOtp()
                )) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }


        /*
         * OTP is correct.
         *
         * Activate account.
         */
        user.setActive(true);


        // Save active user
        userRepository.save(user);


        // Delete used OTP
        otpRepository.delete(otp);


        return VerifyOtpResponse.builder()
                .message(
                        "Email verified successfully"
                )
                .build();
    }


    /* =========================================================
       LOGIN
    ========================================================= */

    @Override
    public LoginResponse login(
            LoginRequest loginRequest
    ) {

        // Find user by email
        User user =
                userRepository
                        .findByEmail(
                                loginRequest.getEmail()
                        )
                        .orElseThrow(() ->
                                new NotException(
                                        "Email not found"
                                )
                        );


        /*
         * Check account status.
         */
        if (!user.isActive()) {

            throw new NotException(
                    "This account has been deactivated."
            );
        }


        // Check password
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        )) {

            throw new EmailAndPasswordAreNotMatch(
                    "Email and password do not match"
            );
        }


        // Generate JWT
        String token =
                jwtService.generateToken(user);


        // Create login response
        LoginResponse response =
                userMapper.loginResponse(user);


        // Set JWT token
        response.setToken(token);


        return response;
    }


    /* =========================================================
       FORGOT PASSWORD
    ========================================================= */

    @Override
    @Transactional
    public String forgotPassword(
            ForgotPasswordRequest request
    ) {

        // Find user
        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new NotException(
                                        "Email not found"
                                )
                        );


        /*
         * Find existing reset token
         * or create new one.
         */
        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByUser(user)
                        .orElseGet(() ->
                                PasswordResetToken.builder()
                                        .user(user)
                                        .build()
                        );


        // Generate token
        String token =
                UUID.randomUUID().toString();


        // Set token
        resetToken.setToken(token);


        // Token expires after 15 minutes
        resetToken.setExpiryDate(
                LocalDateTime.now()
                        .plusMinutes(15)
        );


        // Mark token as unused
        resetToken.setUsed(false);


        // Save token
        passwordResetTokenRepository.save(
                resetToken
        );


        // Create reset link
        String resetLink =
                frontendUrl
                        + "/reset-password?token="
                        + token;


        // Send email
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetLink
        );


        return "Password reset link has been sent to your email";
    }


    /* =========================================================
       RESET PASSWORD
       Forgot-password email flow
    ========================================================= */

    @Override
    @Transactional
    public String resetPassword(
            ResetPasswordRequest request
    ) {

        // Find reset token
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository
                        .findByToken(
                                request.getToken()
                        )
                        .orElseThrow(() ->
                                new NotException(
                                        "Invalid reset token"
                                )
                        );


        // Check expiry
        if (passwordResetToken
                .getExpiryDate()
                .isBefore(
                        LocalDateTime.now()
                )) {

            passwordResetTokenRepository.delete(
                    passwordResetToken
            );

            throw new NotException(
                    "This reset link has expired"
            );
        }


        if (!request.getNewPassword()
                .equals(
                        request.getConfirmPassword()
                )) {

            throw new NotException(
                    "New password and confirm password do not match"
            );
        }

        if (passwordResetToken.isUsed()) {

            throw new NotException(
                    "This reset link has already been used"
            );
        }


        User user =
                passwordResetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );


        userRepository.save(user);

        passwordResetTokenRepository.delete(
                passwordResetToken
        );


        return "Password has been changed successfully";
    }

    @Override
    @Transactional
    public String changePassword(
            String email,
            ChangePasswordRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new NotException(
                                        "User not found"
                                )
                        );

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new NotException(
                    "Current password is incorrect"
            );
        }

        if (request.getNewPassword() == null ||
                request.getNewPassword().isBlank()) {

            throw new NotException(
                    "New password cannot be empty"
            );
        }

        if (!request.getNewPassword()
                .equals(
                        request.getConfirmPassword()
                )) {

            throw new NotException(
                    "New password and confirm password do not match"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {

            throw new NotException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
        return "Password changed successfully";
    }
}