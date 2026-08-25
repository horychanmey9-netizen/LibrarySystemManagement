package com.example.LibraryBack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineSummaryResponse {
    private BigDecimal totalFines;

    private BigDecimal unpaid;

    private BigDecimal paid;

    private Integer totalLateDays;
}
