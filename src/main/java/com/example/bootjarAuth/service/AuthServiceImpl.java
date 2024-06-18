package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        //이메일 중복 제한
        if(authRepository.existsByEmail(signUpRequest.getEmail()))
            throw new IllegalArgumentException("중복된 이메일입니다.");
        //닉네임 중복 제한
        if(authRepository.existsByNickname(signUpRequest.getNickname()))
            throw new IllegalArgumentException("중복된 닉네임입니다.");

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        authRepository.save(signUpRequest.toEntity(encodedPassword));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
//        Optional<User> optionalUser = authRepository.findByEmail(loginRequest.getEmail());
//        if(optionalUser.isEmpty()) throw new IllegalArgumentException("존재하지 않는 Email 입니다.");
//
//        User user = optionalUser.get();
//        if(!user.getPassword().equals(loginRequest.getPassword())) throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
//
//        String token = jwtUtil.generateToken(loginRequest.getEmail());
//        return LoginResponse.from(token);

        User byEmail = authRepository.findByEmail(loginRequest.getEmail());
        if(byEmail == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword()))
            throw new IllegalArgumentException("Email 혹은 비밀번호가 틀렸습니다.");

        String token = jwtUtil.generateToken(loginRequest.getEmail());
        return LoginResponse.from(token);
    }

    @Override
    public UserResponse getUser(String token) {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");

        return  UserResponse.from(user);
    }

    @Override
    public void deleteUser(UserDto userDto) {
        authRepository.deleteById(userDto.getUserId());
    }

    @Transactional
    @Override
    public void updateUser(UpdateDto updateDto) {
        User user =  authRepository.findById(updateDto.getUserId()).orElseThrow(()-> new IllegalArgumentException("해당하는 회원이 없습니다"));

        user.setNickname(updateDto.getNickname());
        user.setPassword(updateDto.getPassword());
    }
}