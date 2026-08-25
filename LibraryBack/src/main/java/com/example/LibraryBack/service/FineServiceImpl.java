package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.dto.response.FineSummaryResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Fine;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.FineStatus;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.FineMapper;
import com.example.LibraryBack.repositoy.BookRepository;
import com.example.LibraryBack.repositoy.FineRepository;
import com.example.LibraryBack.repositoy.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineMapper fineMapper;

    @Override
    public FineResponse create(FineRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotException("Book not found"));

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new NotException("User not found"));

        Fine fine = fineMapper.toEntity(request);

        fine.setBook(book);
        fine.setUser(user);

        fine.setStatus(FineStatus.UNPAID);
        calculateFine(fine);
        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toResponse(savedFine);
    }


    @Override
    @Transactional(readOnly = true)
    public List<FineResponse> getFine() {

        return fineRepository.findAll()
                .stream()
                .map(fineMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public FineResponse getById(Long id) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        return fineMapper.toResponse(fine);
    }


    @Override
    public FineResponse pay(Long id) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        if (fine.getStatus() == FineStatus.PAID) {
            throw new NotException("Fine is already paid");
        }

        fine.setStatus(FineStatus.PAID);
        fine.setPaidAt(LocalDateTime.now());

        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toResponse(savedFine);
    }


    @Override
    public FineResponse update(Long id, FineRequest request) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        Book book = bookRepository
                .findById(request.getBookId())
                .orElseThrow(() ->
                        new NotException("Book not found")
                );

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new NotException("User not found")
                );

        // Update normal fields
        fineMapper.updateEntity(request, fine);

        // Update relationships
        fine.setBook(book);
        fine.setUser(user);

        // Recalculate days late and total amount
        calculateFine(fine);

        Fine updatedFine = fineRepository.save(fine);

        return fineMapper.toResponse(updatedFine);
    }


    @Override
    public void delete(Long id) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        fineRepository.delete(fine);
    }


    private void calculateFine(Fine fine) {

        if (fine.getDueDate() == null ||
                fine.getReturnedDate() == null ||
                fine.getFineRate() == null) {

            fine.setDaysLate(0);
            fine.setTotalAmount(BigDecimal.ZERO);
            return;
        }

        long lateDays = ChronoUnit.DAYS.between(
                fine.getDueDate(),
                fine.getReturnedDate()
        );

        if (lateDays < 0) {
            lateDays = 0;
        }

        fine.setDaysLate((int) lateDays);

        BigDecimal totalAmount = fine.getFineRate()
                .multiply(BigDecimal.valueOf(lateDays));

        fine.setTotalAmount(totalAmount);
    }


    @Override
    @Transactional(readOnly = true)
    public FineSummaryResponse getSummary() {

        List<Fine> fines = fineRepository.findAll();

        BigDecimal totalFines = fines.stream()
                .map(Fine::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal unpaid = fines.stream()
                .filter(fine ->
                        fine.getStatus() == FineStatus.UNPAID
                )
                .map(Fine::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paid = fines.stream()
                .filter(fine ->
                        fine.getStatus() == FineStatus.PAID
                )
                .map(Fine::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalLateDays = fines.stream()
                .mapToInt(Fine::getDaysLate)
                .sum();

        return FineSummaryResponse.builder()
                .totalFines(totalFines)
                .unpaid(unpaid)
                .paid(paid)
                .totalLateDays(totalLateDays)
                .build();
    }

}