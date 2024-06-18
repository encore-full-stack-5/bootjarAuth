package com.example.bootjarAuth.dto.Response;

public record LoginResponse (
        String token,
        String tokenType,
        String message
){
    public static LoginResponse from(String token){
        return new LoginResponse(token, "Bearer", "로그인 성공");
    }
}
