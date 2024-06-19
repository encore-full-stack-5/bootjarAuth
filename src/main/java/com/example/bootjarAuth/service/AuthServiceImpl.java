package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GcsService gcsService;

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
        User byEmail = authRepository.findByEmail(loginRequest.getEmail());
        if(byEmail == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword()))
            throw new IllegalArgumentException("Email 혹은 비밀번호가 틀렸습니다.");

        String token = jwtUtil.generateToken(byEmail.getId(), loginRequest.getEmail());
        return LoginResponse.from(token);
    }

    @Override
    public UserResponse getUser(String token) {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");

        return  UserResponse.from(user);
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        authRepository.deleteByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
    }

    @Transactional
    @Override
    public void updateUser(String token, UpdateDto updateDto) throws IOException {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw  new IllegalArgumentException("해당하는 회원이 없습니다");

        String imageUrl = gcsService.uploadFile(updateDto.getImage().getOriginalFilename(), updateDto.getImage().getBytes());


        user.setNickname(updateDto.getNickname());
        if (updateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        user.setImage(imageUrl);

    }

    @Override
    public List<SearchResponse> searchUser(String nickname) {
       return authRepository.findByNicknameContaining(nickname).stream().map(SearchResponse::from).toList();
    }
}