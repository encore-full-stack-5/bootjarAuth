package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.ChangePasswordRequest;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.QRTokenRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.TokenResponse;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.service.AuthService;
import com.example.bootjarAuth.service.EmailService;
import com.example.bootjarAuth.service.QRCodeService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final QRCodeService qrCodeService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(
            @Validated @RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }



    @PostMapping("/qrcode/token")
    public ResponseEntity<TokenResponse> generateQRToken(@RequestBody QRTokenRequest qrTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(qrCodeService.generateQRToken(qrTokenRequest.getEmail()));
    }

    // qrCode
    @PostMapping("/qrcode")
    public void sendQRCodeEmail(@RequestBody EmailDto emailDto) throws IOException, WriterException, MessagingException {
        // QR코드 생성
        byte[] qrCode = qrCodeService.generateQRCodeImage(emailDto.getAddress(), emailDto.getChangePasswordUrl());
        // 이메일 및 QR코드 전송
        emailService.sendEmail(emailDto.getAddress(), qrCode);
    }

    @PostMapping("/change/password")
    public void changePassword(@RequestBody @Validated ChangePasswordRequest changePasswordRequest){

        authService.changePassword(changePasswordRequest);
    }
}
