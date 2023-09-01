package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthRefreshTokenResponse {
    private String accessToken;
    private String tokenType;
}
