package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.dto.response.FineSummaryResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Borrower;
import com.example.LibraryBack.entity.Fine;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.FineStatus;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.FineMapper;
import com.example.LibraryBack.repository.BookRepository;
import com.example.LibraryBack.repository.FineRepository;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineMapper fineMapper;


    // =========================================================
    // CREATE FINE
    // =========================================================
    @Override
    public FineResponse create(
            FineRequest request
    ) {

        Book book =
                bookRepository.findById(
                        request.getBookId()
                ).orElseThrow(() ->
                        new NotException(
                                "Book not found"
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


        Fine fine =
                fineMapper.toEntity(
                        request
                );


        fine.setBook(book);

        fine.setUser(user);

        fine.setStatus(
                FineStatus.UNPAID
        );


        calculateFine(fine);


        Fine savedFine =
                fineRepository.save(
                        fine
                );


        return fineMapper.toResponse(
                savedFine
        );
    }


    // =========================================================
    // GET ALL FINES
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public List<FineResponse> getFine() {

        return fineRepository.findAll()
                .stream()
                .map(fineMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET FINE BY ID
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public FineResponse getById(Long id) {

        Fine fine =
                fineRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Fine not found"
                                )
                        );


        return fineMapper.toResponse(
                fine
        );
    }


    // =========================================================
    // OLD PAY METHOD
    //
    // Keep this method so old code will not break.
    // =========================================================
    @Override
    public FineResponse pay(Long id) {

        Fine fine =
                fineRepository.findById(id)
                        .orElseThrow(() ->
                                new NotException(
                                        "Fine not found"
                                )
                        );


        // =====================================================
        // CHECK IF ALREADY PAID
        // =====================================================

        if (fine.getStatus()
                == FineStatus.PAID) {

            throw new NotException(
                    "Fine is already paid"
            );
        }


        // =====================================================
        // SET PAID
        // =====================================================

        fine.setStatus(
                FineStatus.PAID
        );


        fine.setPaidAt(
                LocalDateTime.now()
        );


        Fine savedFine =
                fineRepository.save(
                        fine
                );


        return fineMapper.toResponse(
                savedFine
        );
    }


    // =========================================================
    // PAY FINE WHEN ADMIN ACCEPTS RETURN
    //
    // User returns book
    //        ↓
    // RETURN_REQUESTED
    //        ↓
    // Admin accepts
    //        ↓
    // Check Fine
    //
    // If Fine exists:
    //      Calculate final amount
    //      PAID
    //
    // If Fine does not exist:
    //      Check late days
    //
    //      On time:
    //          No Fine
    //
    //      Late:
    //          Create Fine
    //          PAID
    //
    // IMPORTANT:
    // This method NEVER throws
    // "Fine not found for this borrowing".
    //
    // This prevents the transaction rollback issue.
    // =========================================================
    @Override
    public FineResponse payByBorrowing(
            Long userId,
            Long bookId,
            LocalDate dueDate,
            LocalDate returnedDate
    ) {


        // =====================================================
        // FIND EXISTING FINE
        // =====================================================

        Optional<Fine> existingFine =
                fineRepository
                        .findByUserIdAndBookIdAndDueDate(
                                userId,
                                bookId,
                                dueDate
                        );


        // =====================================================
        // FINE ALREADY EXISTS
        // =====================================================

        if (existingFine.isPresent()) {

            Fine fine =
                    existingFine.get();


            // =================================================
            // SET ACTUAL RETURN DATE
            // =================================================

            fine.setReturnedDate(
                    returnedDate
            );


            // =================================================
            // CALCULATE FINAL FINE
            // =================================================

            calculateFine(
                    fine
            );


            // =================================================
            // ADMIN ACCEPTS RETURN
            // PAYMENT RECEIVED
            // =================================================

            fine.setStatus(
                    FineStatus.PAID
            );


            fine.setPaidAt(
                    LocalDateTime.now()
            );


            Fine savedFine =
                    fineRepository.save(
                            fine
                    );


            return fineMapper.toResponse(
                    savedFine
            );
        }


        // =====================================================
        // NO FINE EXISTS
        // =====================================================

        long lateDays =
                ChronoUnit.DAYS.between(
                        dueDate,
                        returnedDate
                );


        // =====================================================
        // RETURNED ON TIME
        //
        // No Fine is required.
        // =====================================================

        if (lateDays <= 0) {

            return null;
        }


        // =====================================================
        // RETURNED LATE
        //
        // Fine does not exist yet.
        // Create it now.
        // =====================================================

        User user =
                userRepository.findById(
                        userId
                ).orElseThrow(() ->
                        new NotException(
                                "User not found"
                        )
                );


        Book book =
                bookRepository.findById(
                        bookId
                ).orElseThrow(() ->
                        new NotException(
                                "Book not found"
                        )
                );


        // =====================================================
        // CREATE NEW FINE
        // =====================================================

        Fine fine =
                new Fine();


        fine.setUser(
                user
        );


        fine.setBook(
                book
        );


        fine.setDueDate(
                dueDate
        );


        fine.setReturnedDate(
                returnedDate
        );


        // =====================================================
        // FINE RATE
        //
        // Temporary:
        // 1000 per late day
        // =====================================================

        fine.setFineRate(
                BigDecimal.valueOf(
                        1000
                )
        );


        // =====================================================
        // CALCULATE FINE
        // =====================================================

        calculateFine(
                fine
        );


        // =====================================================
        // ADMIN ACCEPTS RETURN
        // PAYMENT RECEIVED
        // =====================================================

        fine.setStatus(
                FineStatus.PAID
        );


        fine.setPaidAt(
                LocalDateTime.now()
        );


        // =====================================================
        // SAVE FINE
        // =====================================================

        Fine savedFine =
                fineRepository.save(
                        fine
                );


        return fineMapper.toResponse(
                savedFine
        );
    }


    // =========================================================
    // UPDATE FINE
    // =========================================================
    @Override
    public FineResponse update(
            Long id,
            FineRequest request
    ) {

        Fine fine =
                fineRepository.findById(
                        id
                ).orElseThrow(() ->
                        new NotException(
                                "Fine not found"
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


        User user =
                userRepository.findById(
                        request.getUserId()
                ).orElseThrow(() ->
                        new NotException(
                                "User not found"
                        )
                );


        fineMapper.updateEntity(
                request,
                fine
        );


        fine.setBook(
                book
        );


        fine.setUser(
                user
        );


        calculateFine(
                fine
        );


        Fine updatedFine =
                fineRepository.save(
                        fine
                );


        return fineMapper.toResponse(
                updatedFine
        );
    }


    // =========================================================
    // DELETE FINE
    // =========================================================
    @Override
    public void delete(Long id) {

        Fine fine =
                fineRepository.findById(
                        id
                ).orElseThrow(() ->
                        new NotException(
                                "Fine not found"
                        )
                );


        fineRepository.delete(
                fine
        );
    }


    // =========================================================
    // CREATE FINE FROM BORROWING
    //
    // Called when a borrowing becomes OVERDUE.
    //
    // Existing Fine:
    //      Do not create duplicate.
    //
    // No Fine:
    //      Create new Fine.
    // =========================================================
    @Override
    public FineResponse createFromBorrowing(
            Borrower borrower
    ) {

        Optional<Fine> existingFine =
                fineRepository
                        .findByUserIdAndBookIdAndDueDate(
                                borrower.getUser().getId(),
                                borrower.getBook().getId(),
                                borrower.getDueDate()
                        );


        // =====================================================
        // FINE ALREADY EXISTS
        // =====================================================

        if (existingFine.isPresent()) {

            return fineMapper.toResponse(
                    existingFine.get()
            );
        }


        // =====================================================
        // CREATE NEW FINE
        // =====================================================

        Fine fine =
                new Fine();


        fine.setUser(
                borrower.getUser()
        );


        fine.setBook(
                borrower.getBook()
        );


        fine.setDueDate(
                borrower.getDueDate()
        );


        fine.setReturnedDate(
                null
        );


        // =====================================================
        // TEMPORARY FINE RATE
        // 1000 PER LATE DAY
        // =====================================================

        fine.setFineRate(
                BigDecimal.valueOf(
                        1000
                )
        );


        // =====================================================
        // NEW FINE = UNPAID
        // =====================================================

        fine.setStatus(
                FineStatus.UNPAID
        );


        // =====================================================
        // CALCULATE CURRENT FINE
        // =====================================================

        calculateFine(
                fine
        );


        Fine savedFine =
                fineRepository.save(
                        fine
                );


        return fineMapper.toResponse(
                savedFine
        );
    }


    // =========================================================
    // CALCULATE FINE
    //
    // If returnedDate exists:
    //
    //      dueDate -> returnedDate
    //
    // If returnedDate is null:
    //
    //      dueDate -> today
    // =========================================================
    private void calculateFine(
            Fine fine
    ) {

        // =====================================================
        // INVALID DATA
        // =====================================================

        if (fine.getDueDate() == null
                ||
                fine.getFineRate() == null) {

            fine.setDaysLate(
                    0
            );


            fine.setTotalAmount(
                    BigDecimal.ZERO
            );


            return;
        }


        // =====================================================
        // DETERMINE END DATE
        // =====================================================

        LocalDate endDate =
                fine.getReturnedDate() != null
                        ? fine.getReturnedDate()
                        : LocalDate.now();


        // =====================================================
        // CALCULATE LATE DAYS
        // =====================================================

        long lateDays =
                ChronoUnit.DAYS.between(
                        fine.getDueDate(),
                        endDate
                );


        // =====================================================
        // NOT LATE
        // =====================================================

        if (lateDays < 0) {

            lateDays = 0;
        }


        // =====================================================
        // SAVE DAYS LATE
        // =====================================================

        fine.setDaysLate(
                (int) lateDays
        );


        // =====================================================
        // CALCULATE TOTAL
        //
        // Total = Rate × Days Late
        // =====================================================

        BigDecimal totalAmount =
                fine.getFineRate()
                        .multiply(
                                BigDecimal.valueOf(
                                        lateDays
                                )
                        );


        fine.setTotalAmount(
                totalAmount
        );
    }


    // =========================================================
    // FINE SUMMARY
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public FineSummaryResponse getSummary() {

        List<Fine> fines =
                fineRepository.findAll();


        // =====================================================
        // TOTAL FINES
        // =====================================================

        BigDecimal totalFines =
                fines.stream()
                        .map(Fine::getTotalAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // =====================================================
        // UNPAID
        // =====================================================

        BigDecimal unpaid =
                fines.stream()
                        .filter(fine ->
                                fine.getStatus()
                                        == FineStatus.UNPAID
                        )
                        .map(Fine::getTotalAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // =====================================================
        // PAID
        // =====================================================

        BigDecimal paid =
                fines.stream()
                        .filter(fine ->
                                fine.getStatus()
                                        == FineStatus.PAID
                        )
                        .map(Fine::getTotalAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        // =====================================================
        // TOTAL LATE DAYS
        // =====================================================

        Integer totalLateDays =
                fines.stream()
                        .mapToInt(fine ->
                                fine.getDaysLate() != null
                                        ? fine.getDaysLate()
                                        : 0
                        )
                        .sum();


        // =====================================================
        // RESPONSE
        // =====================================================

        return FineSummaryResponse
                .builder()
                .totalFines(
                        totalFines
                )
                .unpaid(
                        unpaid
                )
                .paid(
                        paid
                )
                .totalLateDays(
                        totalLateDays
                )
                .build();
    }
}