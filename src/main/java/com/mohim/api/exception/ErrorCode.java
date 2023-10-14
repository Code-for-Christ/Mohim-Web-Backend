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

    // CHURCH 예외
    NOT_FOUND_CHURCH(HttpStatus.NOT_FOUND, "해당 교회를 찾을 수 없습니다."),

    // CHURCH_MEMBER 예외
    NOT_FOUND_CHURCH_MEMBER(HttpStatus.NOT_FOUND, "등록되지 않은 성도 입니다."),
    ALREADY_EXISTS_CHURCH_MEMBER(HttpStatus.BAD_REQUEST, "이미 등록된 성도 입니다."),

    //PROFILE_IMAGE 예외
    NOT_FOUND_PROFILE_IMAGE(HttpStatus.NOT_FOUND, "등록된 프로필 이미지가 없습니다."),
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "유효하지 않는 확장자 입니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 업로드에 실패했습니다."),

    // HOUSEHOLD 예외
    INVALID_SELF_RELATIONSHIP(HttpStatus.BAD_REQUEST, "자신과 세대주의 관계는 본인만 가능합니다."),
    INVALID_RELATIONSHIP(HttpStatus.BAD_REQUEST, "유효하지 않은 세대주와의 관계입니다."),

    // POSITION 예외
    NOT_FOUND_POSITION(HttpStatus.NOT_FOUND, "존재하지 않는 직분입니다."),

    // PARISH 예외
    NOT_FOUND_PARISH(HttpStatus.NOT_FOUND, "존재하지 않는 교구입니다."),
    INVALID_PARISH_OR_CELL(HttpStatus.BAD_REQUEST, "유효하지 않은 교구 혹은 구역입니다."),

    // PARISH_ROLE 예외
    NOT_FOUND_PARISH_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 교구역할입니다."),

    // CELL 예외
    NOT_FOUND_CELL(HttpStatus.NOT_FOUND, "존재하지 않는 구역입니다."),

    // CELL_ROLE 예외
    NOT_FOUND_CELL_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 구역역할입니다."),

    // GATHERING 예외
    NOT_FOUND_GATHERING(HttpStatus.NOT_FOUND, "존재하지 않는 회(모임)입니다."),

    // GATHERING_ROLE 예외
    NOT_FOUND_GATHERING_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 회별역할입니다."),

    // MINISTRY 예외
    NOT_FOUND_MINISTRY(HttpStatus.NOT_FOUND, "존재하지 않는 봉사입니다."),

    // MINISTRY_ROLE 예외
    NOT_FOUND_MINISTRY_ROLE(HttpStatus.NOT_FOUND, "존재하지 않는 봉사역할입니다."),
    ;
    private final HttpStatus status;
    private final String error;
}
