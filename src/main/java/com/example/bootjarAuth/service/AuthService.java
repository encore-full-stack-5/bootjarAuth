package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.LoginRequest;
import com.example.bootjarAuth.dto.SignUpRequest;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    void login(LoginRequest loginRequest);
}
