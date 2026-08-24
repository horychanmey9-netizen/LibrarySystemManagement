package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.FineResponse;
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

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FineResponse> create(
            @RequestBody FineRequest fineRequest) {

        return new ApiResponse<>(
                "Fine created successfully",
                201,
                fineService.create(fineRequest)
        );
    }

    @GetMapping("/getData")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<List<FineResponse>> getFine() {

        return new ApiResponse<>(
                "Get fine data successfully",
                200,
                fineService.getFine()
        );
    }

    @PutMapping("/pay/{id}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<FineResponse> pay(
            @PathVariable Long id) {

        return new ApiResponse<>(
                "Fine paid successfully",
                200,
                fineService.pay(id)
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<FineResponse> update(
            @PathVariable Long id,
            @RequestBody FineRequest fineRequest) {

        return new ApiResponse<>(
                "Fine updated successfully",
                200,
                fineService.update(id, fineRequest)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(
            @PathVariable Long id) {

        fineService.delete(id);

        return new ApiResponse<>(
                "Fine deleted successfully",
                200,
                null
        );
    }
}