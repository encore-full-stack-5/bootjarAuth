package com.example.bootjarAuth.dto.Response;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private Long userId;
    private String nickname;
    private Boolean publicScope;
    private String image;
}
