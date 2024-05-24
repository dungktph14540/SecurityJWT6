package com.blo.security6.mapper;

import com.blo.security6.dto.request.UserCreationRequest;
import com.blo.security6.dto.request.UserUpdateRequest;
import com.blo.security6.dto.response.UserResponse;
import com.blo.security6.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserCreationRequest request);
    void updateUser(@MappingTarget Users users, UserUpdateRequest request);
    UserResponse toUserResponse(Users users);
}
