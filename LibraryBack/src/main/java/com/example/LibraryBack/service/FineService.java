package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.dto.response.FineSummaryResponse;
import com.example.LibraryBack.entity.Borrower;

import java.time.LocalDate;
import java.util.List;

public interface FineService {
    FineResponse create(FineRequest request);
    List<FineResponse> getFine();
    FineResponse getById(Long id);
    FineResponse pay(Long id);
    FineResponse update(Long id, FineRequest request);
    void delete(Long id);
    FineSummaryResponse getSummary();

    FineResponse payByBorrowing(
            Long userId,
            Long bookId,
            LocalDate dueDate,
            LocalDate returnedDate
    );

    FineResponse createFromBorrowing(Borrower borrower);
}
