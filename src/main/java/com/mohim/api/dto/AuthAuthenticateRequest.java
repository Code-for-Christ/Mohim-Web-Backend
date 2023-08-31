package com.mohim.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthAuthenticateRequest {
    private String name;
    private String phoneNumber;
    private Long churchId;
}
