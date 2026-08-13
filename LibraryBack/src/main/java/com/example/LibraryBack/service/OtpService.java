package com.example.LibraryBack.service;

import com.example.LibraryBack.entity.Otp;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.repositoy.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    public String generateOtp(){
        return String.format("%06d", new Random().nextInt(1000000));
    }
    public void createOtp(User user){
        otpRepository.deleteByUser(user);
        String code = generateOtp();
        Otp otp = Otp.builder()
                .otp(code)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .user(user)
                .build();

        otpRepository.save(otp);

        emailService.sendOtp(user.getEmail(), code);
    }
}
