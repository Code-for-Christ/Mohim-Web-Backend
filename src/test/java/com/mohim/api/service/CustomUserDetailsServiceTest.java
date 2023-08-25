package com.mohim.api.service;

import com.mohim.api.repository.AuthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
class CustomUserDetailsServiceTest {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Test
    @DisplayName("유저 이메일로 권환 조회")
    void loadUserByUsername() {
        // given
        UserDetails user = customUserDetailsService.loadUserByUsername("sungkyum1@gmail.com");
        // then
        assertThat(user.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_ADMIN", "WRITE_PERMISSION");
        System.out.println("user's Authorities : " + user.getAuthorities());
    }
}