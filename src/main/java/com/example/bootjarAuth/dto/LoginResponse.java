package com.example.bootjarAuth.dto;

public record LoginResponse (
        String token,
        String tokenType
){
    public static LoginResponse from(String token){
        return new LoginResponse(token, "jwt");
    }
}
