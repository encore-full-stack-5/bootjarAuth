package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.ChangePasswordRequest;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.TokenResponse;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    TokenResponse login(LoginRequest loginRequest);


    void changePassword(ChangePasswordRequest changePasswordRequest);
}
