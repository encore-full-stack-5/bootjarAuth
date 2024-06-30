package com.example.bootjarAuth.dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    private String qrToken;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$"
            , message = "비밀번호는 영어와 숫자 , 특수문자를 포함해서 8자리 이상으로 입력해주세요.")
    private String password;
}
