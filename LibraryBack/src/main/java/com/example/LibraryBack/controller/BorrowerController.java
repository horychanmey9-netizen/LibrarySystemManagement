package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.BorrowerRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.BorrowerResponse;
import com.example.LibraryBack.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrower")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<BorrowerResponse> create(
            @RequestBody BorrowerRequest borrowerRequest) {

        return new ApiResponse<>(
                "Borrow book successfully",
                201,
                borrowerService.create(borrowerRequest)
        );
    }

    @GetMapping("/getData")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<List<BorrowerResponse>> getData() {

        return new ApiResponse<>(
                "Get borrowing data successfully",
                200,
                borrowerService.getData()
        );
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<BorrowerResponse> getById(
            @PathVariable Long id) {

        return new ApiResponse<>(
                "Get borrowing successfully",
                200,
                borrowerService.getById(id)
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BorrowerResponse> update(
            @PathVariable Long id,
            @RequestBody BorrowerRequest borrowerRequest) {
        return new ApiResponse<>(
                "Borrowing updated successfully",
                200,
                borrowerService.update(id, borrowerRequest)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(
            @PathVariable Long id) {
        borrowerService.delete(id);
        return new ApiResponse<>(
                "Borrowing deleted successfully",
                200,
                null
        );
    }
}
