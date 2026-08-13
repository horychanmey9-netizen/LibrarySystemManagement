package com.example.LibraryBack.dto.request;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
}
