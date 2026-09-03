package com.example.LibraryBack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("Hello,\n\n" + "You requested to reset your password.\n\n"
                + "Please click the link below to create a new password:\n\n"
                + resetLink + "\n\n" + "This link will expire in 15 minutes.\n\n"
                + "If you did not request a password reset, please ignore this email.\n\n"
                + "Library System");
        mailSender.send(message);
    }
}
