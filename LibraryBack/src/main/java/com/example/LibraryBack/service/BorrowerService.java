package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.BorrowerRequest;
import com.example.LibraryBack.dto.response.BorrowerResponse;

import java.util.List;

public interface BorrowerService {
    BorrowerResponse create(BorrowerRequest request);
    List<BorrowerResponse> getData();
    BorrowerResponse getById(Long id);
    BorrowerResponse accept(Long id);
    BorrowerResponse reject(Long id);
    BorrowerResponse update(Long id, BorrowerRequest request);
    void delete(Long id);
}
