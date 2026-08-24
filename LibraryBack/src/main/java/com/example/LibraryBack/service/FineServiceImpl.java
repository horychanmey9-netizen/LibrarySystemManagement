package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.entity.Book;
import com.example.LibraryBack.entity.Fine;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.enums.FineStatus;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.FineMapper;
import com.example.LibraryBack.repositoy.BookRepository;
import com.example.LibraryBack.repositoy.FineRepository;
import com.example.LibraryBack.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final FineMapper fineMapper;

    @Override
    public FineResponse create(FineRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new NotException("Book not found")
                );

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new NotException("User not found")
                );

        Fine fine = fineMapper.toEntity(request);

        fine.setBook(book);
        fine.setUser(user);
        fine.setStatus(FineStatus.Unpaid);

        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toResponse(savedFine);
    }

    @Override
    public List<FineResponse> getFine() {

        return fineRepository.findAll()
                .stream()
                .map(fineMapper::toResponse)
                .toList();
    }

    @Override
    public FineResponse pay(Long id) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        fine.setStatus(FineStatus.Paid);

        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toResponse(savedFine);
    }

    @Override
    public FineResponse update(Long id, FineRequest request) {

        Fine fine = fineRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("Fine not found")
                );

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new NotException("Book not found")
                );

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new NotException("User not found")
                );

        fine.setBook(book);
        fine.setUser(user);

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
}