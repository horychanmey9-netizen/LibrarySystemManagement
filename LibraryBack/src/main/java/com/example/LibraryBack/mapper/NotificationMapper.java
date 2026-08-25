package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.NotificationRequest;
import com.example.LibraryBack.dto.response.NotificationResponse;
import com.example.LibraryBack.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "isStarred", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    Notification toEntity(NotificationRequest request);

    @Mapping(target = "userId", source = "user.id")
    NotificationResponse toResponse(Notification notification);
}
