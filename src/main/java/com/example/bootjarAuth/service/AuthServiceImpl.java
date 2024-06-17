package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.global.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository
                .findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User user = signUpRequest.toEntity(encodedPassword);
        authRepository.save(user);
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

        Optional<User> byEmail = authRepository.findByEmail(loginRequest.getEmail());
        if(byEmail.isEmpty() ||
                !passwordEncoder.matches(loginRequest.getPassword(), byEmail.get().getPassword()))
            throw new IllegalArgumentException("Email 혹은 비밀번호가 틀렸습니다.");

        String token = jwtUtil.generateToken(loginRequest.getEmail());
        return LoginResponse.from(token);
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
