package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    void login(LoginRequest loginRequest);

    UserResponse getUser(UserDto userDto);

    void deleteUser(UserDto userDto);

    void updateUser(UpdateDto updateDto);
}
