package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthAuthenticateResponse {
    private String email;
    private Long id;
    private Long churchId;
    private Long churchMemberId;
    private boolean isAuthenticated;
}
