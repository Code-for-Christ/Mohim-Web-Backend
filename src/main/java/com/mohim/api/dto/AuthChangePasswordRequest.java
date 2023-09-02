package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthChangePasswordRequest {
    private String email;
    private String password;
    private String temporaryCode;
}
