package com.mohim.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class AuthJoinRequest {

    @NotBlank(message = "이메일을 입력하세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
}