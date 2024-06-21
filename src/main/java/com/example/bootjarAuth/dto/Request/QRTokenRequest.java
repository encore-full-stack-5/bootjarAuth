package com.example.bootjarAuth.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QRTokenRequest {
    private String email;
}
