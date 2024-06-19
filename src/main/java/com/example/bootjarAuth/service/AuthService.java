package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponse getUser(String token);

    void deleteUser(UserDto userDto);

    void updateUser(UpdateDto updateDto);
}
