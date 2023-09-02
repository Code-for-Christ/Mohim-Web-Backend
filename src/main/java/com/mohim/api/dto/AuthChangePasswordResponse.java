package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthChangePasswordResponse {
    private String message;
}
