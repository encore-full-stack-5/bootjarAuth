package com.example.bootjarAuth.domain;

import com.example.bootjarAuth.dto.Response.SearchResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthRepository extends JpaRepository<User ,Long> {
    User findByEmail(String email);
    void deleteByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    List<User>  findByNicknameContaining(String nickname);
}
