package com.example.LibraryBack.service;

import com.example.LibraryBack.entity.Borrower;
import com.example.LibraryBack.enums.BorrowingStatus;
import com.example.LibraryBack.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingOverdueService {

    private final BorrowerRepository borrowerRepository;
    private final FineService fineService;

    @Transactional
    public void checkOverdueBorrowings() {

        LocalDate today = LocalDate.now();

        List<Borrower> overdueBorrowers =
                borrowerRepository.findByStatusAndDueDateBefore(
                        BorrowingStatus.BORROWED,
                        today
                );

        for (Borrower borrower : overdueBorrowers) {

            // Change BORROWED → OVERDUE
            borrower.setStatus(BorrowingStatus.OVERDUE);

            // Create Fine if it does not already exist
            fineService.createFromBorrowing(borrower);

            borrowerRepository.save(borrower);
        }
    }
}