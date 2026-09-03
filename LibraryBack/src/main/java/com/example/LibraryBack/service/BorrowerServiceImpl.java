package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BorrowerRequest;
import com.example.LibraryBack.dto.response.BorrowerResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Borrower;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.BorrowerMapper;
import com.example.LibraryBack.repository.BookRepository;
import com.example.LibraryBack.repository.BorrowerRepository;
import com.example.LibraryBack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowerMapper borrowerMapper;

    @Override
    @Transactional
    public BorrowerResponse create(BorrowerRequest borrowerRequest) {

        User user = userRepository.findById(borrowerRequest.getUserId())
                .orElseThrow(() -> new NotException("User not found"));

        Book book = bookRepository.findById(borrowerRequest.getBookId())
                .orElseThrow(() -> new NotException("Book not found"));

        Borrower borrower = borrowerMapper.toEntity(borrowerRequest);

        borrower.setUser(user);
        borrower.setBook(book);

        if (borrower.getFine() == null) {
            borrower.setFine(BigDecimal.ZERO);
        }

        Borrower savedBorrower = borrowerRepository.save(borrower);

        return borrowerMapper.toResponse(savedBorrower);
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

        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new NotException("Borrower not found"));

        return borrowerMapper.toResponse(borrower);
    }

    @Override
    @Transactional
    public BorrowerResponse update(Long id, BorrowerRequest request) {

        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new NotException("Borrower not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotException("Book not found"));

        borrower.setUser(user);
        borrower.setBook(book);
        borrower.setBorrowDate(request.getBorrowDate());
        borrower.setDueDate(request.getDueDate());
        borrower.setReturnDate(request.getReturnDate());
        borrower.setStatus(request.getStatus());

        if (borrower.getFine() == null) {
            borrower.setFine(BigDecimal.ZERO);
        }

        Borrower updatedBorrower = borrowerRepository.save(borrower);

        return borrowerMapper.toResponse(updatedBorrower);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new NotException("Borrower not found"));

        borrowerRepository.delete(borrower);
    }
}
