package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BorrowerRequest;
import com.example.LibraryBack.dto.response.BorrowerResponse;
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
    // After RETURNED, user can borrow the same book again.
    //
    // Quantity DOES NOT decrease here.
    // Quantity decreases only when ADMIN accepts.
    // =========================================================
    @Override
    @Transactional
    public BorrowerResponse create(BorrowerRequest borrowerRequest) {

        User user = userRepository.findById(
                borrowerRequest.getUserId()
        ).orElseThrow(() ->
                new NotException("User not found")
        );

        Book book = bookRepository.findById(
                borrowerRequest.getBookId()
        ).orElseThrow(() ->
                new NotException("Book not found")
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
        //
        // Same User
        // +
        // Same Book
        // +
        // Active Status
        //
        // => Cannot create another request
        // =====================================================

        List<BorrowingStatus> activeStatuses = List.of(

                BorrowingStatus.PENDING,

                BorrowingStatus.BORROWED,

                BorrowingStatus.RETURN_REQUESTED,

                BorrowingStatus.OVERDUE

        );


        boolean alreadyBorrowing =
                borrowerRepository.existsByUserIdAndBookIdAndStatusIn(
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
    @Override
    public List<BorrowerResponse> getData() {

        return borrowerRepository.findAll()
                .stream()
                .map(borrowerMapper::toResponse)
                .toList();
    }

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
        telegramNotificationService.sendBorrowAcceptedNotification(
                borrower.getUser().getId(),
                book.getTitle(),
                borrower.getDueDate()
        );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }

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

        if (borrower.getStatus()
                != BorrowingStatus.BORROWED) {

            throw new NotException(
                    "Only BORROWED books can be returned"
            );
        }


        // =====================================================
        // BORROWED -> RETURN_REQUESTED
        // =====================================================
        borrower.setStatus(
                BorrowingStatus.RETURN_REQUESTED
        );


        // =====================================================
        // DO NOT INCREASE QUANTITY HERE
        // DO NOT SET RETURN DATE HERE
        // =====================================================

        Borrower updatedBorrower =
                borrowerRepository.save(
                        borrower
                );


        return borrowerMapper.toResponse(
                updatedBorrower
        );
    }



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

        if (borrower.getStatus()
                != BorrowingStatus.RETURN_REQUESTED) {

            throw new NotException(
                    "Only RETURN_REQUESTED books can be accepted"
            );
        }


        Book book = borrower.getBook();


        if (book == null) {

            throw new NotException(
                    "Book not found"
            );
        }


        // =====================================================
        // INCREASE QUANTITY
        //
        // The book is physically returned.
        // =====================================================
        book.setQty(
                book.getQty() + 1
        );


        bookRepository.save(book);


        // =====================================================
        // SET ACTUAL RETURN DATE
        // =====================================================
        borrower.setReturnDate(
                LocalDate.now()
        );

        borrower.setStatus(
                BorrowingStatus.RETURNED
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
}