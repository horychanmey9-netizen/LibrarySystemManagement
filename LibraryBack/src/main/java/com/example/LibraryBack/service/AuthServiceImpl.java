package com.example.LibraryBack.service;

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
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExists("Email already exists");
        }

        User user = userMapper.toRegister(registerRequest);

        user.setPassword(
                passwordEncoder.encode(registerRequest.getPassword())
        );

        user.setRole(Role.USER);
        user.setActive(false);

        user = userRepository.save(user);

        otpService.createOtp(user);

        return userMapper.registerResponse(user);
    }


    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new NotException("Email not found")
                );

        Otp otp = otpRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("OTP not found")
                );

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!otp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setActive(true);

        userRepository.save(user);

        otpRepository.delete(otp);

        return VerifyOtpResponse.builder()
                .message("Email verified successfully")
                .build();
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new NotException("Email not found")
                );

        if (!user.isActive()) {
            throw new RuntimeException(
                    "Please verify your email first."
            );
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        )) {
            throw new EmailAndPasswordAreNotMatch(
                    "Email and password do not match"
            );
        }

        String token = jwtService.generateToken(user);

        LoginResponse response =
                userMapper.loginResponse(user);

        response.setToken(token);

        return response;
    }


    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new NotException("Email not found")
                );
        passwordResetTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate =
                LocalDateTime.now().plusMinutes(15);

        PasswordResetToken passwordResetToken =
                PasswordResetToken.builder()
                        .token(token)
                        .user(user)
                        .expiryDate(expiryDate)
                        .used(false)
                        .build();

        passwordResetTokenRepository.save(passwordResetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetLink
        );

        return "Password reset link has been sent to your email";
    }


    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest request) {

        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository
                        .findByToken(request.getToken())
                        .orElseThrow(() ->
                                new NotException(
                                        "Invalid reset token"
                                )
                        );

        // Check if token was already used
        if (passwordResetToken.isUsed()) {
            throw new NotException(
                    "This reset link has already been used"
            );
        }

        // Check token expiration
        if (passwordResetToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new NotException(
                    "This reset link has expired"
            );
        }

        // Check password confirmation
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new NotException(
                    "New password and confirm password do not match"
            );
        }

        // Get user
        User user = passwordResetToken.getUser();

        // Encode new password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        // Mark token as used
        passwordResetToken.setUsed(true);

        passwordResetTokenRepository.save(passwordResetToken);

        return "Password has been changed successfully";
    }
}