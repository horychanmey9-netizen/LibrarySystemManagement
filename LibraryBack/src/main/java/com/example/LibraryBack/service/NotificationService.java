package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.NotificationRequest;
import com.example.LibraryBack.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse create(NotificationRequest request);
    List<NotificationResponse> getAll();
    List<NotificationResponse> getByUserId(Long userId);
    List<NotificationResponse> getUnread(Long userId);
    List<NotificationResponse> getStarred(Long userId);
    long countUnread(Long userId);
    NotificationResponse getById(Long id);
    NotificationResponse markAsRead(Long id);
    NotificationResponse markAsUnread(Long id);
    NotificationResponse toggleStar(Long id);
    NotificationResponse update(Long id, NotificationRequest request);
    void delete(Long id);
    void deleteAllByUserId(Long userId);
}
