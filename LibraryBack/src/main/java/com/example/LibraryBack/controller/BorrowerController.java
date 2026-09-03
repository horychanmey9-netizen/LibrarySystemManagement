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


    // =========================================================
    // CREATE BORROW REQUEST
    // USER clicks "Borrow"
    // Status will be PENDING
    // =========================================================
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ApiResponse<BorrowerResponse> create(
            @RequestBody BorrowerRequest borrowerRequest) {

        return new ApiResponse<>(
                "Borrow request created successfully",
                201,
                borrowerService.create(borrowerRequest)
        );
    }


    // =========================================================
    // GET ALL BORROWINGS
    // ADMIN / USER
    // =========================================================
    @GetMapping("/getData")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApiResponse<List<BorrowerResponse>> getData() {

        return new ApiResponse<>(
                "Get borrowing data successfully",
                200,
                borrowerService.getData()
        );
    }


    // =========================================================
    // GET BORROWING BY ID
    // ADMIN / USER
    // =========================================================
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


    // =========================================================
    // ACCEPT BORROW REQUEST
    // ADMIN ONLY
    //
    // PENDING -> BORROWED
    // Book quantity will decrease in Service
    // =========================================================
    @PutMapping("/accept/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BorrowerResponse> accept(
            @PathVariable Long id) {

        return new ApiResponse<>(
                "Borrow request accepted successfully",
                200,
                borrowerService.accept(id)
        );
    }


    // =========================================================
    // REJECT BORROW REQUEST
    // ADMIN ONLY
    //
    // PENDING -> REJECTED
    // Book quantity will NOT decrease
    // =========================================================
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BorrowerResponse> reject(
            @PathVariable Long id) {

        return new ApiResponse<>(
                "Borrow request rejected successfully",
                200,
                borrowerService.reject(id)
        );
    }


    // =========================================================
    // UPDATE BORROWING
    // ADMIN ONLY
    // =========================================================
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


    // =========================================================
    // DELETE BORROWING
    // ADMIN ONLY
    // =========================================================
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