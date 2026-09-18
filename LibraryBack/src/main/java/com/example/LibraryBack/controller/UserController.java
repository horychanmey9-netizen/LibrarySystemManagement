//package com.example.LibraryBack.controller;
//
//import com.example.LibraryBack.dto.response.ApiResponse;
//import com.example.LibraryBack.dto.response.UserResponse;
//import com.example.LibraryBack.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
//    @GetMapping("/getData")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<List<UserResponse>> getData(){
//        return new ApiResponse<>("Get users sucessfully",200,userService.getData());
//    }
//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<?> deleteUser(
//            @PathVariable Long id
//    ) {
//
//        userService.deleteUser(id);
//
//        return new ApiResponse<>(
//                "User deleted successfully",
//                200,
//                null
//        );
//    }
//
//}
package com.example.LibraryBack.controller;

import com.example.LibraryBack.dto.response.ApiResponse;
import com.example.LibraryBack.dto.response.UserResponse;
import com.example.LibraryBack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getData")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getData() {
        return new ApiResponse<>(
                "Get users successfully",
                200,
                userService.getData()
        );
    }

    @PatchMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> deactivateUser(
            @PathVariable Long id
    ) {

        userService.deactivateUser(id);

        return new ApiResponse<>(
                "User account deactivated successfully",
                200,
                null
        );
    }
    @PatchMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> activateUser(@PathVariable Long id) {

        userService.activateUser(id);

        return new ApiResponse<>(
                "User account activated successfully",
                200,
                null
        );
    }

}
