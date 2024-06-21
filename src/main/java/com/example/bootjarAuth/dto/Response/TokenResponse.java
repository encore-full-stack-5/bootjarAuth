package com.example.bootjarAuth.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
       private String token;
       private String tokenType;

    public static TokenResponse from(String token){
        return new TokenResponse(token, "jwt");
    }
}
