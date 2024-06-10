package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.LoginRequest;
import com.example.bootjarAuth.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthRepository authRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        authRepository.save(signUpRequest.toEntity());
    }

    @Override
    public void login(LoginRequest loginRequest) {
        Optional<User> optionalUser = authRepository.findByEmail(loginRequest.getEmail());

        if(optionalUser.isEmpty()) throw new IllegalArgumentException("존재하지 않는 Email 입니다.");

        User user = optionalUser.get();

        if(!user.getPassword().equals(loginRequest.getPassword())) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");

    }
}
