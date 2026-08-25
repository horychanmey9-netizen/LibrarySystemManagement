package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.request.NotificationRequest;
import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.NotificationResponse;
import com.example.LibraryBack.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ApiResponse<NotificationResponse> create(
            @RequestBody NotificationRequest request
    ) {
        return new ApiResponse<>(
                "Notification created successfully",
                201,
                notificationService.create(request)
        );
    }

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getData() {
        return new ApiResponse<>(
                "Get data successfully",
                200,
                notificationService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getById(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                "Get notification successfully",
                200,
                notificationService.getById(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<NotificationResponse>> getByUserId(
            @PathVariable Long userId
    ) {
        return new ApiResponse<>(
                "Get user notifications successfully",
                200,
                notificationService.getByUserId(userId)
        );
    }

    @GetMapping("/user/{userId}/unread")
    public ApiResponse<List<NotificationResponse>> getUnread(
            @PathVariable Long userId
    ) {
        return new ApiResponse<>(
                "Get unread notifications successfully",
                200,
                notificationService.getUnread(userId)
        );
    }

    @GetMapping("/user/{userId}/starred")
    public ApiResponse<List<NotificationResponse>> getStarred(
            @PathVariable Long userId
    ) {
        return new ApiResponse<>(
                "Get starred notifications successfully",
                200,
                notificationService.getStarred(userId)
        );
    }

    @GetMapping("/user/{userId}/unread/count")
    public ApiResponse<Long> countUnread(
            @PathVariable Long userId
    ) {
        return new ApiResponse<>(
                "Get unread notification count successfully",
                200,
                notificationService.countUnread(userId)
        );
    }

    @PutMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markAsRead(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                "Notification marked as read successfully",
                200,
                notificationService.markAsRead(id)
        );
    }

    @PutMapping("/{id}/unread")
    public ApiResponse<NotificationResponse> markAsUnread(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                "Notification marked as unread successfully",
                200,
                notificationService.markAsUnread(id)
        );
    }

    @PutMapping("/{id}/star")
    public ApiResponse<NotificationResponse> toggleStar(
            @PathVariable Long id
    ) {
        return new ApiResponse<>(
                "Notification star status updated successfully",
                200,
                notificationService.toggleStar(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<NotificationResponse> update(
            @PathVariable Long id,
            @RequestBody NotificationRequest request
    ) {
        return new ApiResponse<>(
                "Notification updated successfully",
                200,
                notificationService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        notificationService.delete(id);

        return new ApiResponse<>(
                "Notification deleted successfully",
                200,
                null
        );
    }

    @DeleteMapping("/user/{userId}")
    public ApiResponse<Void> deleteAllByUserId(
            @PathVariable Long userId
    ) {
        notificationService.deleteAllByUserId(userId);

        return new ApiResponse<>(
                "All user notifications deleted successfully",
                200,
                null
        );
    }
}