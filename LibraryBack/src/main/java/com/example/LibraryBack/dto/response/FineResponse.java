package com.example.LibraryBack.dto.response;

import com.example.LibraryBack.enums.FineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private Integer daysLate;
    private BigDecimal fineRate;
    private BigDecimal totalAmount;
    private FineStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
