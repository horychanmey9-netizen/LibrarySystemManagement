package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BorrowerRequest;
import com.example.LibraryBack.dto.response.BorrowerResponse;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Borrower;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.BorrowingStatus;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.BorrowerMapper;
import com.example.LibraryBack.repository.BookRepository;
import com.example.LibraryBack.repository.BorrowerRepository;
import com.example.LibraryBack.repository.UserRepository;
import com.example.LibraryBack.telegram.TelegramNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowerMapper borrowerMapper;
    private final TelegramNotificationService telegramNotificationService;
    private final FineService fineService;


    // =========================================================
    // CREATE BORROW REQUEST
    //
    // User clicks Borrow
    //
    // IMPORTANT RULE:
    // The same USER cannot borrow the same BOOK again
    // while the previous borrowing is still active.
    //
    // Active statuses:
    // PENDING
    // BORROWED
    // RETURN_REQUESTED
    // OVERDUE
    //
    // RETURNED is NOT active.
    //
    // Quantity DOES NOT decrease here.
    // Quantity decreases only when ADMIN accepts.
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse create(
            BorrowerRequest borrowerRequest
    ) {

        User user =
                userRepository.findById(
                        borrowerRequest.getUserId()
                ).orElseThrow(() ->
                        new NotException(
                                "User not found"
                        )
                );

        Book book =
                bookRepository.findById(
                        borrowerRequest.getBookId()
                ).orElseThrow(() ->
                        new NotException(
                                "Book not found"
                        )
                );


        // =====================================================
        // CHECK IF BOOK IS AVAILABLE
        // =====================================================

        if (book.getQty() <= 0) {

            throw new NotException(
                    "This book is currently unavailable"
            );
        }


        // =====================================================
        // CHECK EXISTING ACTIVE BORROWING
        // =====================================================

        List<BorrowingStatus> activeStatuses =
                List.of(
                        BorrowingStatus.PENDING,
                        BorrowingStatus.BORROWED,
                        BorrowingStatus.RETURN_REQUESTED,
                        BorrowingStatus.OVERDUE
                );


        boolean alreadyBorrowing =
                borrowerRepository
                        .existsByUserIdAndBookIdAndStatusIn(
                                user.getId(),
                                book.getId(),
                                activeStatuses
                        );


        if (alreadyBorrowing) {

            throw new NotException(
                    "You cannot borrow this book again until you return it."
            );
        }


        // =====================================================
        // CREATE NEW BORROWER
        // =====================================================

        Borrower borrower =
                borrowerMapper.toEntity(
                        borrowerRequest
                );


        borrower.setUser(user);

        borrower.setBook(book);


        // =====================================================
        // ALWAYS PENDING WHEN USER REQUESTS
        // =====================================================

        borrower.setStatus(
                BorrowingStatus.PENDING
        );


        // =====================================================
        // DEFAULT FINE
        // =====================================================

        borrower.setFine(
                BigDecimal.ZERO
        );


        // =====================================================
        // SAVE REQUEST
        // =====================================================

        Borrower savedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                savedBorrower
        );
    }


    // =========================================================
    // GET ALL BORROWINGS
    // =========================================================
    @Override
    public List<BorrowerResponse> getData() {

        return borrowerRepository.findAll()
                .stream()
                .map(borrowerMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BORROWING BY ID
    // =========================================================
    @Override
    public BorrowerResponse getById(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrower not found"
                                )
                        );


        return borrowerMapper.toResponse(
                borrower
        );
    }


    // =========================================================
    // ADMIN ACCEPT BORROW
    //
    // PENDING -> BORROWED
    // Book quantity -1
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse accept(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrowing not found"
                                )
                        );


        // =====================================================
        // ONLY PENDING CAN BE ACCEPTED
        // =====================================================

        if (borrower.getStatus()
                != BorrowingStatus.PENDING) {

            throw new NotException(
                    "Only PENDING requests can be accepted"
            );
        }


        Book book = borrower.getBook();


        if (book == null) {

            throw new NotException(
                    "Book not found"
            );
        }


        // =====================================================
        // CHECK QUANTITY AGAIN
        // =====================================================

        if (book.getQty() <= 0) {

            throw new NotException(
                    "Book is no longer available"
            );
        }


        // =====================================================
        // DECREASE QUANTITY
        // =====================================================

        book.setQty(
                book.getQty() - 1
        );


        bookRepository.save(book);


        // =====================================================
        // PENDING -> BORROWED
        // =====================================================

        borrower.setStatus(
                BorrowingStatus.BORROWED
        );


        // =====================================================
        // MAKE SURE FINE IS NOT NULL
        // =====================================================

        if (borrower.getFine() == null) {

            borrower.setFine(
                    BigDecimal.ZERO
            );
        }


        // =====================================================
        // SAVE BORROWING
        // =====================================================

        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        // =====================================================
        // TELEGRAM NOTIFICATION
        // =====================================================

        telegramNotificationService
                .sendBorrowAcceptedNotification(
                        borrower.getUser().getId(),
                        book.getTitle(),
                        borrower.getDueDate()
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }


    // =========================================================
    // ADMIN REJECT BORROW
    //
    // PENDING -> REJECTED
    //
    // Book quantity DOES NOT change.
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse reject(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrowing not found"
                                )
                        );


        // =====================================================
        // ONLY PENDING CAN BE REJECTED
        // =====================================================

        if (borrower.getStatus()
                != BorrowingStatus.PENDING) {

            throw new NotException(
                    "Only PENDING requests can be rejected"
            );
        }


        // =====================================================
        // PENDING -> REJECTED
        // =====================================================

        borrower.setStatus(
                BorrowingStatus.REJECTED
        );


        // =====================================================
        // MAKE SURE FINE IS NOT NULL
        // =====================================================

        if (borrower.getFine() == null) {

            borrower.setFine(
                    BigDecimal.ZERO
            );
        }


        // =====================================================
        // DO NOT CHANGE BOOK QUANTITY
        // =====================================================

        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }


    // =========================================================
    // UPDATE BORROWING
    //
    // ADMIN
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse update(
            Long id,
            BorrowerRequest request
    ) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrower not found"
                                )
                        );


        User user =
                userRepository.findById(
                        request.getUserId()
                ).orElseThrow(() ->
                        new NotException(
                                "User not found"
                        )
                );


        Book book =
                bookRepository.findById(
                        request.getBookId()
                ).orElseThrow(() ->
                        new NotException(
                                "Book not found"
                        )
                );


        borrower.setUser(user);

        borrower.setBook(book);

        borrower.setBorrowDate(
                request.getBorrowDate()
        );

        borrower.setDueDate(
                request.getDueDate()
        );

        borrower.setReturnDate(
                request.getReturnDate()
        );

        borrower.setStatus(
                request.getStatus()
        );


        if (borrower.getFine() == null) {

            borrower.setFine(
                    BigDecimal.ZERO
            );
        }


        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }


    // =========================================================
    // DELETE BORROWING
    // =========================================================
    @Override
    @Transactional
    public void delete(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrower not found"
                                )
                        );


        borrowerRepository.delete(
                borrower
        );
    }


    // =========================================================
    // USER REQUEST RETURN
    //
    // BORROWED -> RETURN_REQUESTED
    //
    // OVERDUE -> RETURN_REQUESTED
    //
    // Book quantity DOES NOT increase here.
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse returnBook(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrowing not found"
                                )
                        );


        // =====================================================
        // ALLOW BORROWED AND OVERDUE
        // =====================================================

        if (borrower.getStatus()
                != BorrowingStatus.BORROWED
                &&
                borrower.getStatus()
                        != BorrowingStatus.OVERDUE) {

            throw new NotException(
                    "Only BORROWED or OVERDUE books can be returned"
            );
        }


        // =====================================================
        // BORROWED / OVERDUE
        //        ↓
        // RETURN_REQUESTED
        // =====================================================

        borrower.setStatus(
                BorrowingStatus.RETURN_REQUESTED
        );


        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }


    // =========================================================
    // ADMIN ACCEPT RETURN
    //
    // RETURN_REQUESTED
    //        ↓
    // Fine processing
    //        ↓
    // Book quantity +1
    //        ↓
    // RETURNED
    //
    // IMPORTANT:
    // Admin accepting return means:
    // 1. Book received
    // 2. Fine payment received if overdue
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse acceptReturn(Long id) {

        Borrower borrower =
                borrowerRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Borrowing not found"
                                )
                        );


        // =====================================================
        // ONLY RETURN_REQUESTED CAN BE ACCEPTED
        // =====================================================

        if (borrower.getStatus()
                != BorrowingStatus.RETURN_REQUESTED) {

            throw new NotException(
                    "Only RETURN_REQUESTED books can be accepted"
            );
        }


        User user =
                borrower.getUser();

        Book book =
                borrower.getBook();


        // =====================================================
        // CHECK USER
        // =====================================================

        if (user == null) {

            throw new NotException(
                    "User not found"
            );
        }


        // =====================================================
        // CHECK BOOK
        // =====================================================

        if (book == null) {

            throw new NotException(
                    "Book not found"
            );
        }


        // =====================================================
        // ACTUAL RETURN DATE
        // =====================================================

        LocalDate returnedDate =
                LocalDate.now();


        // =====================================================
        // PROCESS FINE
        //
        // If Fine exists:
        //      Calculate final amount
        //      Set PAID
        //
        // If Fine does not exist:
        //      If overdue:
        //          Create Fine
        //          Set PAID
        //
        //      If on time:
        //          No Fine
        //
        // IMPORTANT:
        // No try/catch here.
        //
        // This avoids the transaction rollback problem.
        // =====================================================

        FineResponse paidFine =
                fineService.payByBorrowing(
                        user.getId(),
                        book.getId(),
                        borrower.getDueDate(),
                        returnedDate
                );


        // =====================================================
        // SAVE FINE AMOUNT TO BORROWER
        // =====================================================

        if (paidFine != null
                && paidFine.getTotalAmount() != null) {

            borrower.setFine(
                    paidFine.getTotalAmount()
            );

        } else {

            borrower.setFine(
                    BigDecimal.ZERO
            );
        }


        // =====================================================
        // INCREASE BOOK QUANTITY
        // =====================================================

        book.setQty(
                book.getQty() + 1
        );


        bookRepository.save(
                book
        );


        // =====================================================
        // SET RETURN DATE
        // =====================================================

        borrower.setReturnDate(
                returnedDate
        );


        // =====================================================
        // RETURN_REQUESTED -> RETURNED
        // =====================================================

        borrower.setStatus(
                BorrowingStatus.RETURNED
        );


        // =====================================================
        // SAVE BORROWING
        // =====================================================

        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }
}