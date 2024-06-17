package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.dto.*;
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

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto){
        authService.deleteUser(userDto);
        return ResponseEntity.ok("삭제 성공");
    }
    @GetMapping("/me")
    public UserResponse getUser(@RequestBody UserDto userDto){
    return authService.getUser(userDto);
    }


    @PutMapping("/me")
    public ResponseEntity<String> updateUser(@RequestBody UpdateDto updateDto){
        authService.updateUser(updateDto);
        return ResponseEntity.ok("수정 성공");
    }
}
