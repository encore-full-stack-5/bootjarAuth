package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.dto.LoginRequest;
import com.example.bootjarAuth.dto.SignUpRequest;
import com.example.bootjarAuth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(
            @Validated @RequestBody SignUpRequest signUpRequest){
        authService.signUp(signUpRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        authService.login(loginRequest);
        return ResponseEntity.ok("로그인 성공");
    }
//
//    @DeleteMapping("/me")
//    public ResponseEntity<String> deleteUser(){
//
//    }
//    @GetMapping("/me")
//    public User getUser(){
//
//    }
//    @PutMapping("/me")
//    public ResponseEntity<String> updateUser(){
//
//    }
}
