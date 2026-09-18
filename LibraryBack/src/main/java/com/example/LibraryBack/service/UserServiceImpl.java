//package com.example.LibraryBack.service;
//
//import com.example.LibraryBack.dto.response.UserResponse;
//import com.example.LibraryBack.mapper.UserMapper;
//import com.example.LibraryBack.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//
//    @Override
//    public List<UserResponse> getData() {
//        return userRepository.findAll()
//                .stream()
//                .map(userMapper::userResponse)
//                .toList();
//    }
//    @Override
//    public void deleteUser(Long id) {
//
//        userRepository.deleteById(id);
//
//    }
//}
package com.example.LibraryBack.service;

import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.entity.User;
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

    @Override
    public void deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );

        user.setActive(false);

        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        )
                );

        user.setActive(true);
        userRepository.save(user);
    }
}
