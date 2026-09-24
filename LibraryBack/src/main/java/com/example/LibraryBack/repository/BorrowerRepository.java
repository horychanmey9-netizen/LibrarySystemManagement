package com.example.LibraryBack.repository;

import com.example.LibraryBack.entity.Borrower;
import com.example.LibraryBack.enums.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowerRepository extends JpaRepository<Borrower,Long> {
    boolean existsByUserIdAndBookIdAndStatusIn(
            Long userId,
            Long bookId,
            List<BorrowingStatus> statuses
    );
    List<Borrower> findByStatusAndDueDateBefore(
            BorrowingStatus status,
            LocalDate date
    );

}
