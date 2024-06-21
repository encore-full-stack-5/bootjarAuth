package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;
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

    UserResponse getUser(String token);

    void deleteUser(String token);

    void updateUser(String bearerToken, UpdateDto updateDto) throws IOException;

    List<SearchResponse> searchUser(String nickname);

    // QR Code
    TokenResponse generateQRToken(String email);
    byte[] generateQRCodeImage(String email, String changePasswordUrl) throws WriterException, IOException;
    // Email
    void sendEmail(String address, byte[] qrCode) throws IOException, MessagingException;
}
