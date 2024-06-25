package com.example.bootjarAuth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="Users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    @Setter
    private String password;

    @Column(name = "user_nickname")
    @Setter
    private String nickname;

    @Column(name = "user_image")
    @Setter
    private String image;

    @Column(name = "user_public_scope", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean publicScope;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}

