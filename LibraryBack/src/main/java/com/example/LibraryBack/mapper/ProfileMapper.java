package com.example.LibraryBack.mapper;

import com.example.LibraryBack.dto.request.ProfileRequest;
import com.example.LibraryBack.dto.response.ProfileResponse;
import com.example.LibraryBack.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "image", ignore = true)
    Profile toEntity(ProfileRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    ProfileResponse toResponse(Profile profile);
}
