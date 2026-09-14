package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.TelegramAccountRequest;
import com.example.LibraryBack.dto.response.TelegramAccountResponse;
import com.example.LibraryBack.entity.TelegramAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TelegramAccountMapper {
    @Mapping(target = "user", ignore = true)
    TelegramAccount toEntity(TelegramAccountRequest accountRequest);

    @Mapping(target = "userId", source = "user.id")
    TelegramAccountResponse toResponse(TelegramAccount account);
}