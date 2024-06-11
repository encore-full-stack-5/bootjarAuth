package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Override
    public UserResponse getUser(UserDto userDto) {

        //본인 확인 로칙 추가 해야 함

        User user = authRepository.findById(userDto.getUserId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다"));

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
