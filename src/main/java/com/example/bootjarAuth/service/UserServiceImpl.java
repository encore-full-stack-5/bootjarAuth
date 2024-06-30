package com.example.bootjarAuth.service;


import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserInfoResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.dto.UpdateDto;
import com.example.bootjarAuth.global.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthRepository authRepository;
    private final GcsService gcsService;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getUser(String token) {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");

        log.info("image = " + user.getImage());

        return  UserResponse.from(user);
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        String email = jwtUtil.getByEmailFromTokenAndValidate(token);
        User user = authRepository.findByEmail(email);

        sendDeleteUserInfo(user);

        authRepository.deleteById(user.getId());


    }

    @Transactional
    @Override
    public void updateUser(String token, UpdateDto updateDto) throws IOException {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw  new IllegalArgumentException("존재하지 않는 회원입니다.");

        if(user.getNickname().equals(updateDto.getNickname())) throw  new IllegalArgumentException("중복된 이메일입니다.");


        if (updateDto.getImage() != null && !updateDto.getImage().isEmpty()) {
            String imageUrl = gcsService.uploadFile(updateDto.getImage().getOriginalFilename(), updateDto.getImage().getBytes());
            user.setImage(imageUrl);
        } else {
            user.setImage(user.getImage());
        }

        if (updateDto.getPassword() != null) user.setPassword(passwordEncoder.encode(updateDto.getPassword()));

        if (updateDto.getNickname() != null) user.setNickname(updateDto.getNickname());

        if (updateDto.getUserPublicScope() != null) user.setPublicScope(updateDto.getUserPublicScope());

        sendUpdateUserInfo(user);
    }

    @Override
    public List<SearchResponse> searchUser(String nickname) {
        return authRepository.findByNicknameContaining(nickname).stream().map(SearchResponse::from).toList();
    }




    public void sendDeleteUserInfo(User deletedUser) {
        deleteUserInfoFromFriendsService(deleteUserInfoResponse(deletedUser).getUserId());

        deleteUserInfoFromTodosService(deleteUserInfoResponse(deletedUser).getUserId());

        deleteUserInfoFromCommentsService(deleteUserInfoResponse(deletedUser).getUserId());


    }

    public void sendUpdateUserInfo(User updatedUser) {
        updateUserInfoInFriendsService(updateUserInfoResponse(updatedUser));

        updateUserInfoInTodosService(TodoUpdateUserInfo(updatedUser));

        updateUserInfoInCommentsService(updateUserInfoResponse(updatedUser));

    }



    public UserInfoResponse deleteUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .build();
    }

    public UserInfoResponse updateUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .image(user.getImage())
                .build();
    }

    public UserInfoResponse TodoUpdateUserInfo(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .publicScope(user.getPublicScope())
                .build();
    }




    public void updateUserInfoInTodosService(UserInfoResponse userInfoResponse) {
        restTemplate.put(
                "http://35.238.87.27/todos/user-update ",
                userInfoResponse
        );
    }

    public void updateUserInfoInFriendsService(UserInfoResponse userInfoResponse) {
        restTemplate.put(
                "http://34.173.194.250/friends/user-update",
                userInfoResponse
        );
    }

    public void updateUserInfoInCommentsService(UserInfoResponse userInfoResponse) {
        restTemplate.put(
                "http://34.31.174.33/todos/comments/user-update ",
                userInfoResponse
        );
    }

    public void deleteUserInfoFromTodosService(Long userId) {
        restTemplate.delete(
                "http://35.238.87.27/todos/user-delete/{userId}",
                userId
        );
    }

    public void deleteUserInfoFromFriendsService(Long userId) {
        restTemplate.delete(
                "http://34.173.194.250/friends/user-delete/{userId}",
                userId
        );
    }

    public void deleteUserInfoFromCommentsService(Long userId) {
        restTemplate.delete(
                "http://34.31.174.33/todos/comments/user-delete/{userId}",
                userId
        );
    }

}
