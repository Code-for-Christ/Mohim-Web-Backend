package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class AuthLoginResponse {
    private String accessToken;

    private String email;

    List<String> role;
}