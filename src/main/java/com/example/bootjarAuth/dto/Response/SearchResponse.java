package com.example.bootjarAuth.dto.Response;

import com.example.bootjarAuth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {
    private Long userId;
    private String nickname;
    private Boolean userPublicScope;


    public static SearchResponse from(User user) {
        return SearchResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .userPublicScope(user.getPublicScope())
                .build();
    }
}
