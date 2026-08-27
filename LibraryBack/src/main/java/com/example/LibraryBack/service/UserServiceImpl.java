package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.request.UserRequest;
import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.entity.Profile;
import com.example.LibraryBack.entity.User;
import com.example.LibraryBack.exception.NotException;
import com.example.LibraryBack.mapper.UserMapper;
import com.example.LibraryBack.repositoy.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUser() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::userResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + id)
                );

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());

        Profile profile = user.getProfile();

        if (profile != null) {
            profile.setPhone(userRequest.getPhone());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.userResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotException("User not found")
                );

        userRepository.delete(user);
    }
}