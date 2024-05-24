package com.blo.security6.controller;

import com.blo.security6.dto.request.ApiResponse;
import com.blo.security6.dto.request.UserCreationRequest;
import com.blo.security6.dto.request.UserUpdateRequest;
import com.blo.security6.dto.response.UserResponse;
import com.blo.security6.entity.Users;
import com.blo.security6.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping("users")
    ApiResponse<List<UserResponse>> getUsers() {
       var authentication = SecurityContextHolder.getContext().getAuthentication();
       log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @PostMapping("users")
     ApiResponse<UserResponse> createUsers(@Valid  @RequestBody UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createRequest(request));
        return apiResponse;
    }
    @GetMapping("users/{userId}")
    UserResponse detailUser(@PathVariable("userId") String id){
        return userService.detailUser(id);
    }
    @PutMapping("users/update/{userId}")
    UserResponse update(@RequestBody UserUpdateRequest request, @PathVariable("userId") String id){
        return userService.updateUser(request,id);
    }
    @DeleteMapping("users/delete/{userId}")
    String delete(@PathVariable("userId") String id){
        userService.deleteUser(id);
        return "User has been delete";
    }
}
