package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.FineRequest;
import com.example.LibraryBack.dto.response.FineResponse;
import com.example.LibraryBack.entity.Fine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "daysLate", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Fine toEntity(FineRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    FineResponse toResponse(Fine fine);

}