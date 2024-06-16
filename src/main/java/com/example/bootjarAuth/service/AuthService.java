package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.*;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponse getUser(UserDto userDto);

    void deleteUser(UserDto userDto);

    void updateUser(UpdateDto updateDto);
}
