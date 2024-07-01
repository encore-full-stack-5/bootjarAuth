package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.Request.ChangePasswordRequest;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.TokenResponse;
import com.example.bootjarAuth.dto.Response.UserInfoResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;


    @Override
    public void signUp(SignUpRequest signUpRequest) {
        //이메일 중복 제한
        if(authRepository.existsByEmail(signUpRequest.getEmail()))
            throw new IllegalArgumentException("중복된 이메일입니다.");
        //닉네임 중복 제한
        if(authRepository.existsByNickname(signUpRequest.getNickname()))
            throw new IllegalArgumentException("중복된 닉네임입니다.");

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User saveuser = authRepository.save(signUpRequest.toEntity(encodedPassword));

        sendSaveUserInfo(saveuser);

    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        User byEmail = authRepository.findByEmail(loginRequest.getEmail());
        if(byEmail == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword()))
            throw new IllegalArgumentException("Email 혹은 비밀번호가 틀렸습니다.");

        String token = jwtUtil.generateToken(byEmail.getId(), loginRequest.getEmail(), byEmail.getNickname(), byEmail.getImage());
        return TokenResponse.from(token);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            String email = jwtUtil.getByEmailFromTokenAndValidate(changePasswordRequest.getQrToken());
            User user = authRepository.findByEmail(email);
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        } catch (ExpiredJwtException error) {
            throw new IllegalArgumentException("만료된 URL입니다.");
        }
    }


    public void sendSaveUserInfo(User savedUser) {
        sendUserInfoToFriendsService(createUserInfoResponse(savedUser));

        sendUserInfoToTodosService(TodoCreateUserInfo(savedUser));

        sendUserInfoToCommentsService(createUserInfoResponse(savedUser));


    }

    public void sendUserInfoToTodosService(UserInfoResponse userInfoResponse) {
        restTemplate.postForEntity(
                "http://35.238.87.27/todos/user-signup",
                userInfoResponse,
                UserInfoResponse.class
        );
    }

    public void sendUserInfoToFriendsService(UserInfoResponse userInfoResponse) {
        restTemplate.postForEntity(
                "http://34.173.194.250/friends/user-signup",
                userInfoResponse,
                UserInfoResponse.class
        );
    }

    public void sendUserInfoToCommentsService(UserInfoResponse userInfoResponse) {
        restTemplate.postForEntity(
                "http://34.31.174.33/todos/comments/user-signup",
                userInfoResponse,
                UserInfoResponse.class
        );
    }

    public UserInfoResponse TodoCreateUserInfo(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .publicScope(user.getPublicScope())
                .build();
    }

    public UserInfoResponse createUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }

}