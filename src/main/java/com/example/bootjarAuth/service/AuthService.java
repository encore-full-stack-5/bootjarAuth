package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponse getUser(String token);

    void deleteUser(String token);

    void updateUser(String bearerToken, UpdateDto updateDto) throws IOException;

    List<SearchResponse> searchUser(String nickname);
}
