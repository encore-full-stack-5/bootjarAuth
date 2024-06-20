package com.example.bootjarAuth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailDto {
    private String address;
    private String changePasswordUrl;
}