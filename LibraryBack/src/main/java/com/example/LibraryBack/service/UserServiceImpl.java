package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.mapper.UserMapper;
import com.example.LibraryBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getData() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userResponse)
                .toList();
    }
}
