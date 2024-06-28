package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.dto.UpdateDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserResponse getUser(String token);

    void deleteUser(String token);

    void updateUser(String bearerToken, UpdateDto updateDto) throws IOException;

    List<SearchResponse> searchUser(String nickname);
}
