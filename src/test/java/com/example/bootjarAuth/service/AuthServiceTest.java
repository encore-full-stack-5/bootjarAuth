package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.LoginRequest;
import com.example.bootjarAuth.dto.LoginResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Nested
    @Transactional
    class 로그인 {
        @Test
        void 성공 () {
            // given
            User user = User.builder()
                    .email("test@gmail.com")
                    .nickname("test")
                    .password(passwordEncoder.encode("test1234!@#$"))
                    .publicScope(false)
                    .build();
            authRepository.save(user);
            LoginRequest request = new LoginRequest("test@gmail.com", "test1234!@#$");
            // when
            LoginResponse response = authService.login(request);
            // then
            assertNotNull(response.token());
        }
    }
}