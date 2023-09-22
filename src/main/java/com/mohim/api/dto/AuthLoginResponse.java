package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class AuthLoginResponse {
    private String name;
    private Long churchId;
    private String churchName;
    private Long churchMemberId;
    private String email;
    private List<String> role;
    private String accessToken;
}