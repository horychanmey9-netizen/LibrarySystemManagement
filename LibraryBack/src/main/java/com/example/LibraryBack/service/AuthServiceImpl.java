package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.ChangePasswordRequest;
import com.example.LibraryBack.dto.request.LoginRequest;
import com.example.LibraryBack.dto.request.RegisterRequest;
import com.example.LibraryBack.dto.request.VerifyOtpRequest;
import com.example.LibraryBack.dto.response.LoginResponse;
import com.example.LibraryBack.dto.response.RegisterResponse;
import com.example.LibraryBack.dto.response.VerifyOtpResponse;
import com.example.LibraryBack.entity.Otp;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.Role;
import com.example.LibraryBack.exception.EmailAlreadyExists;
import com.example.LibraryBack.exception.EmailAndPasswordAreNotMatch;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.UserMapper;
import com.example.LibraryBack.repositoy.OtpRepository;
import com.example.LibraryBack.repositoy.UserRepository;
import com.example.LibraryBack.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final UserMapper userMapper;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExists("Email already exists");
        }
        User user = userMapper.toRegister(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setActive(false);
        user = userRepository.save(user);
        otpService.createOtp(user);
        return userMapper.registerResponse(user);
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotException("Email not found"));
        Otp otp = otpRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("OTP not found"));
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
                .orElseThrow(() -> new NotException("Email not found"));
        if (!user.isActive()) {
            throw new RuntimeException(
                    "Please verify your email first."
            );
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new EmailAndPasswordAreNotMatch(
                    "Email and password do not match"
            );
        }
        String token = jwtService.generateToken(user);
        LoginResponse response = userMapper.loginResponse(user);
        response.setToken(token);

        return response;
    }
    @Override
    public String changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotException("Email not found"));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new EmailAndPasswordAreNotMatch(
                    "Current password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
        return "Password has been changed successfully";
    }
}