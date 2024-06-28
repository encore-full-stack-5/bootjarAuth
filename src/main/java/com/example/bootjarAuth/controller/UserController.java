package com.example.bootjarAuth.controller;

import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.dto.UpdateDto;
import com.example.bootjarAuth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token){
        String bearerToken = token.substring(7);
        userService.deleteUser(bearerToken);
        return ResponseEntity.ok("삭제 성공");
    }
    @GetMapping("/me")
    public UserResponse getUser(@RequestHeader("Authorization") String token){
        String bearerToken = token.substring(7);
        return userService.getUser(bearerToken);
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String token,
                                             @Validated @ModelAttribute UpdateDto updateDto) throws IOException {
        String bearerToken = token.substring(7);
        userService.updateUser(bearerToken,updateDto);

        return ResponseEntity.ok("수정 성공");
    }

    @GetMapping("/search")
    public List<SearchResponse> searchUser(@RequestParam("nickname") String nickname){
        return userService.searchUser(nickname);
    }
}

