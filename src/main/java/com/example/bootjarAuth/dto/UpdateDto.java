package com.example.bootjarAuth.dto;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
    private Long userId;
    private String nickname;
    private String password;
}
