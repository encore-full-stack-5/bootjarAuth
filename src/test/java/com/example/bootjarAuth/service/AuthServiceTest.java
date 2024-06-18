package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void 회원가입_성공() {
        // given
        SignUpRequest request = new SignUpRequest("test@example.com", "testNickname", "testPassword", true);
        when(authRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(authRepository.existsByNickname(request.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        // when
        authServiceImpl.signUp(request);
        // then
        verify(authRepository, times(1)).save(any(User.class));// 메서드가 1번 호출 되었는지 확인
    }

    @Test
    void 회원가입_실패_이메일중복() {
        // given
        SignUpRequest request = new SignUpRequest("test@example.com", "testNickname", "testPassword", true);
        when(authRepository.existsByEmail(request.getEmail())).thenReturn(true);
        // when
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.signUp(request));
        // then
        verify(authRepository, never()).save(any(User.class));//호출 절대 못해!
    }
    @Test
    void 회원가입_실패_닉네임중복() {
        // given
        SignUpRequest request = new SignUpRequest("test@example.com", "testNickname", "testPassword", true);
        when(authRepository.existsByNickname(request.getNickname())).thenReturn(true);
        // when
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.signUp(request));
        // then
        verify(authRepository, never()).save(any(User.class));
    }

    @Test
    void 회원가입_비밀번호_암호화() {
        // given
        SignUpRequest request = new SignUpRequest("test@example.com", "testPassword", "testNickName", true);
        User savedUser = new User(1L,request.getEmail(),request.getNickname(),"encodedPassword","이미지",true);
        when(authRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(authRepository.existsByNickname(request.getNickname())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(authRepository.save(any(User.class))).thenReturn(savedUser);
        // when
        authServiceImpl.signUp(request);
        // then
        verify(authRepository, times(1)).save(any(User.class));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);//저장한 객체 캡쳐
        verify(authRepository, times(1)).save(userCaptor.capture());//캡쳐한 데이터 저장
        User savedUserData = userCaptor.getValue();//값을 가져옴
        String encodedPassword = passwordEncoder.encode(request.getPassword());//암호화 하고
        assertThat(savedUserData.getPassword()).isEqualTo(encodedPassword);//동일한지 비교

    }
    @Test
        void 로그인_성공 () {
            // given
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        User validUser = User.builder()
                .email("email@example.com")
                .password("encodedPassword")
                .build();
        when(authRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), validUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn("validToken");
            // when
        LoginResponse loginResponse = authServiceImpl.login(loginRequest);
            // then
        assertThat(loginResponse.getToken()).isEqualTo("validToken");
        }
    @Test
    void 로그인_실패_이메일 () {
        // given
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        User validUser = User.builder()
                .email("email@example.com")
                .password("encodedPassword")
                .build();
        when(authRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(loginRequest));
        // then
        assertThat(exception.getMessage()).isEqualTo("Email 혹은 비밀번호가 틀렸습니다.");
    }

    @Test
    void 로그인_실패_비밀번호 () {
        // given
        LoginRequest loginRequest = new LoginRequest("email@example.com", "password");
        User validUser = User.builder()
                .email("email@example.com")
                .password("encodedPassword")
                .build();
        when(authRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), validUser.getPassword())).thenReturn(false);
        // when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(loginRequest));
        // then
        assertThat(exception.getMessage()).isEqualTo("Email 혹은 비밀번호가 틀렸습니다.");
    }
}