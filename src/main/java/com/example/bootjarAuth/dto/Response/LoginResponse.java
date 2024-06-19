package com.example.bootjarAuth.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
       private String token;
       private String tokenType;

    public static LoginResponse from(String token){
        return new LoginResponse(token, "jwt");
    }
}
