package com.example.LibraryBack.dto.request;

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
public class FineRequest {
    private Long userId;
    private Long bookId;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private BigDecimal fineRate;
    private FineStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
