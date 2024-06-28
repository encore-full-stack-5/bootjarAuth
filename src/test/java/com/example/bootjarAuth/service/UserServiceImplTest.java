package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.dto.UpdateDto;
import com.example.bootjarAuth.global.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private AuthRepository authRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void 회원정보_가져오기_성공 () {
        // given
        String token = "Bearer valid_token";
        String email = "test@example.com";
        User user = new User(1L, "email", "password", "nickname","image",true);
        UserResponse expectedUserResponse = UserResponse.from(user); //Response 개체를 직접 비교하는게 좋음

        //when
        when(jwtUtil.getByEmailFromTokenAndValidate(token)).thenReturn(email);
        when(authRepository.findByEmail(email)).thenReturn(user);

        UserResponse userResponse = userServiceImpl.getUser(token);

        // then
        assertThat(userResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedUserResponse);
        verify(jwtUtil).getByEmailFromTokenAndValidate(token);
        verify(authRepository).findByEmail(email);
    }

    @Test
    void 회원정보_가져오기_실패 () {
        // given
        String token = "Bearer valid_token";
        String email = "test@example.com";

        // when
        when(jwtUtil.getByEmailFromTokenAndValidate(token)).thenReturn(email);
        when(authRepository.findByEmail(email)).thenReturn(null);

        // then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.getUser(token));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 회원입니다.");
    }

    @Test
    void 회원_삭제_성공() {
        // given
        String token = "valid_token";
        String email = "test@example.com";

        // when
        when(jwtUtil.getByEmailFromTokenAndValidate(token)).thenReturn(email);
        doNothing().when(authRepository).deleteByEmail(email);

        userServiceImpl.deleteUser(token);

        // then
        verify(jwtUtil, times(1)).getByEmailFromTokenAndValidate(token);
        verify(authRepository, times(1)).deleteByEmail(email);
    }



    @Test
    void 회원정보_수정_성공() throws IOException {
        // given
        String token = "valid_token";
        String email = "test@example.com";
        User user = new User(1L, "test@example.com", "old_password", "old_nickname", "old_image", true);
        UpdateDto updateDto = new UpdateDto(null, "new_nickname", "new_password", false);

        // when
        when(jwtUtil.getByEmailFromTokenAndValidate(token)).thenReturn(email);
        when(authRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.encode("new_password")).thenReturn("encoded_new_password");
        userServiceImpl.updateUser(token, updateDto);

        // then
        assertThat(user.getNickname()).isEqualTo("new_nickname");
        assertThat(user.getPassword()).isEqualTo("encoded_new_password");
        assertThat(user.getImage()).isEqualTo("old_image");
        assertThat(user.getPublicScope()).isFalse();
    }

    @Test
    void 회원정보_수정_실패(){
        // given
        String token = "valid_token";
        String email = "test@example.com";
        UpdateDto updateDto = new UpdateDto(null, "new_nickname", "new_password", false);

        // when
        when(jwtUtil.getByEmailFromTokenAndValidate(token)).thenReturn(email);
        when(authRepository.findByEmail(email)).thenReturn(null);


        // then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userServiceImpl.updateUser(token,updateDto));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 회원입니다.");
    }


    @Test
    void 회원_검색_성공() {
        // given
        String nickname = "test";
        User user1 = new User(1L, "user1@example.com", "password1", "test1", "image1", true);
        User user2 = new User(2L, "user2@example.com", "password2", "test2", "image2", false);
        List<User> users = Arrays.asList(user1, user2);

        // when
        when(authRepository.findByNicknameContaining(nickname)).thenReturn(users);
        List<SearchResponse> searchResponses = userServiceImpl.searchUser(nickname);

        // then
        assertThat(searchResponses.size()).isEqualTo(2);
        assertThat(searchResponses.get(0).getUserId()).isEqualTo(user1.getId());
        assertThat(searchResponses.get(0).getNickname()).isEqualTo(user1.getNickname());
        assertThat(searchResponses.get(0).getImage()).isEqualTo(user1.getImage());
        assertThat(searchResponses.get(0).getUserPublicScope()).isEqualTo(user1.getPublicScope());
        assertThat(searchResponses.get(1).getUserId()).isEqualTo(user2.getId());
        assertThat(searchResponses.get(1).getNickname()).isEqualTo(user2.getNickname());
        assertThat(searchResponses.get(1).getImage()).isEqualTo(user2.getImage());
        assertThat(searchResponses.get(1).getUserPublicScope()).isEqualTo(user2.getPublicScope());
        verify(authRepository, times(1)).findByNicknameContaining(nickname);
    }

}