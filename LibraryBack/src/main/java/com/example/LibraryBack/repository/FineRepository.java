package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine,Long> {
    Optional<Fine> findByUserIdAndBookIdAndDueDate(
            Long userId,
            Long bookId,
            LocalDate dueDate
    );

    
}
