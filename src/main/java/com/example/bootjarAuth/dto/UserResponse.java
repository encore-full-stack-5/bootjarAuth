package com.example.bootjarAuth.dto;

import com.example.bootjarAuth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String nickname;
    private Boolean userPublicScope;



    public static UserResponse from(User user){
      return UserResponse.builder()
              .email(user.getEmail())
              .nickname(user.getNickname())
              .userPublicScope(user.getPublicScope())
              .build();
    }
}
