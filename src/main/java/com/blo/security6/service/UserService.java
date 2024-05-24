package com.blo.security6.service;

import com.blo.security6.dto.request.UserCreationRequest;
import com.blo.security6.dto.request.UserUpdateRequest;
import com.blo.security6.dto.response.UserResponse;
import com.blo.security6.entity.Users;
import com.blo.security6.enums.Roles;
import com.blo.security6.exception.AppException;
import com.blo.security6.exception.ErrorCode;
import com.blo.security6.mapper.UserMapper;
import com.blo.security6.repository.RepositoryUsers;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j

public class UserService {
    RepositoryUsers usersRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createRequest(UserCreationRequest request){
        if(usersRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Users users = userMapper.toUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role = new HashSet<>();
        role.add(Roles.USER.name());

        users.setRole(role);

        return userMapper.toUserResponse(usersRepository.save(users));

    }
    public List<Users> UserGetAll(){
        return usersRepository.findAll();
    }

    public UserResponse detailUser(String id) {
        return userMapper.toUserResponse(usersRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXITED)));
    }

    public UserResponse updateUser(UserUpdateRequest request, String id) {
        Users users = usersRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXITED));
        userMapper.updateUser(users,request);
        return userMapper.toUserResponse(usersRepository.save(users));

    }

    public void deleteUser(String id) {
        usersRepository.deleteById(id);
    }

    public List<UserResponse> getUsers() {
        return usersRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
}
