package com.mohim.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 예외
    BAD_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // USER 예외
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "등록되지 않은 이메일입니다."),
    UNAUTHORIZED_ID(HttpStatus.UNAUTHORIZED, "아이디가 틀립니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 틀립니다."),
    INVALID_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 임시 코드입니다."),

    // Church 예외
    NOT_FOUND_CHURCH(HttpStatus.NOT_FOUND, "해당 교회를 찾을 수 없습니다."),

    // CHURCH_MEMBER 예외
    NOT_FOUND_CHURCH_MEMBER(HttpStatus.NOT_FOUND, "등록되지 않은 성도 입니다."),
    ALREADY_EXISTS_CHURCH_MEMBER(HttpStatus.BAD_REQUEST, "이미 등록된 성도 입니다."),
    ;
    private final HttpStatus status;
    private final String error;
}
