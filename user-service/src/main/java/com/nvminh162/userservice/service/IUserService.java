package com.nvminh162.userservice.service;

import java.util.List;

import com.nvminh162.userservice.dto.request.UserCreationRequest;
import com.nvminh162.userservice.dto.response.UserResponse;

public interface IUserService {
    UserResponse createUser(UserCreationRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserCreationRequest dto);
    void deleteUser(Long id);
}
