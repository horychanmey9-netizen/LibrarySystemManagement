package com.example.LibraryBack.repositoy;

import com.example.LibraryBack.entity.Otp;
import com.example.LibraryBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUser(User user);
    void deleteByUser(User user);
}
