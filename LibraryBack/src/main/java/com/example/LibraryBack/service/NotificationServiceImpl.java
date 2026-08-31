package com.example.LibraryBack.service;

import com.example.LibraryBack.exception.NotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.LibraryBack.dto.request.NotificationRequest;
import com.example.LibraryBack.dto.response.NotificationResponse;
import com.example.LibraryBack.entity.Notification;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.mapper.NotificationMapper;
import com.example.LibraryBack.repository.NotificationRepository;
import com.example.LibraryBack.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse create(NotificationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + request.getUserId())
                );

        Notification notification = notificationMapper.toEntity(request);

        notification.setUser(user);
        notification.setIsRead(false);
        notification.setIsStarred(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification =
                notificationRepository.save(notification);

        return notificationMapper.toResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAll() {

        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnread(Long userId) {

        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getStarred(Long userId) {

        return notificationRepository
                .findByUserIdAndIsStarredTrueOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long userId) {

        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getById(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found with id: " + id)
                );

        return notificationMapper.toResponse(notification);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found with id: " + id)
                );

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updatedNotification =
                notificationRepository.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse markAsUnread(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotException("Notification not found" ));

        notification.setIsRead(false);
        notification.setReadAt(null);

        Notification updatedNotification = notificationRepository.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse toggleStar(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setIsStarred(!notification.getIsStarred());
        Notification updatedNotification = notificationRepository.save(notification);

        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setReferenceId(request.getReferenceId());

        Notification updatedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponse(updatedNotification);
    }

    @Override
    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found with id: " + id)
                );
        notificationRepository.delete(notification);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.deleteAll(notifications);
    }
}
