package com.example.bootjarAuth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User ,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}
