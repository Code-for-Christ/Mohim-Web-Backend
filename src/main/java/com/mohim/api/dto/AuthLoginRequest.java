package com.mohim.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthLoginRequest {

    private String email;
    private String password;
}