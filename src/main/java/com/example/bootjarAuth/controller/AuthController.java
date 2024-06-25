package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.dto.*;
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

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token){
        String bearerToken = token.substring(7);
        authService.deleteUser(bearerToken);
        return ResponseEntity.ok("삭제 성공");
    }
    @GetMapping("/me")
        public UserResponse getUser(@RequestHeader("Authorization") String token){
        String bearerToken = token.substring(7);
        return authService.getUser(bearerToken);
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String token,
                                             @Validated @ModelAttribute UpdateDto updateDto) throws IOException {
        String bearerToken = token.substring(7);
        authService.updateUser(bearerToken,updateDto);

        return ResponseEntity.ok("수정 성공");
    }

    @GetMapping("/search")
    public List<SearchResponse> searchUser(@RequestParam("nickname") String nickname){
        return authService.searchUser(nickname);
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
}
