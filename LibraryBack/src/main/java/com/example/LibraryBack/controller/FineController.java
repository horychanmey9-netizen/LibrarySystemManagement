package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.dto.response.FineSummaryResponse;
import com.example.LibraryBack.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fine")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping("/createFine")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FineResponse> createFine(
            @RequestBody FineRequest fineRequest) {

        FineResponse response = fineService.create(fineRequest);

        return new ApiResponse<>(
                "Fine created successfully",
                201,
                response
        );
    }

    @GetMapping("/getAllFine")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<List<FineResponse>> getAllFines() {

        List<FineResponse> response = fineService.getFine();

        return new ApiResponse<>(
                "Get all fines successfully",
                200,
                response
        );
    }

    @GetMapping("/getFinebyId/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<FineResponse> getFineById(
            @PathVariable Long id) {

        FineResponse response = fineService.getById(id);

        return new ApiResponse<>(
                "Get fine successfully",
                200,
                response
        );
    }

    @PutMapping("/payFine/{id}/pay")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<FineResponse> payFine(
            @PathVariable Long id) {

        FineResponse response = fineService.pay(id);

        return new ApiResponse<>(
                "Fine paid successfully",
                200,
                response
        );
    }

    @PutMapping("/updateFine/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FineResponse> updateFine(
            @PathVariable Long id,
            @RequestBody FineRequest fineRequest) {

        FineResponse response = fineService.update(id, fineRequest);

        return new ApiResponse<>(
                "Fine updated successfully",
                200,
                response
        );
    }

    @DeleteMapping("/deleteFine/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteFine(
            @PathVariable Long id) {

        fineService.delete(id);

        return new ApiResponse<>(
                "Fine deleted successfully",
                200,
                null
        );
    }

    @GetMapping("/getsummary")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FineSummaryResponse> getSummary() {

        FineSummaryResponse response = fineService.getSummary();

        return new ApiResponse<>(
                "Get fine summary successfully",
                200,
                response
        );
    }
}