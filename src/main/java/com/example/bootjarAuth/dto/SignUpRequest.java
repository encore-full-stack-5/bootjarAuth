package com.example.bootjarAuth.dto;

import com.example.bootjarAuth.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "이메일을 꼭 입력 해주세요")
    @Email(message = "올바른 이메일형식을 지켜주세요")
    private String email;

    @NotBlank(message = "비밀번호를 꼭 입력 해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$"
            , message = "비밀번호는 영어와 숫자 , 특수문자를 포함해서 8자리 이상으로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 꼭 입력 해주세요")
    private String nickname;

    private Boolean userPublicScope;



    public User toEntity(String encodedPassword){
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .publicScope(userPublicScope != null ? userPublicScope : true)
                .build();
    }
}
