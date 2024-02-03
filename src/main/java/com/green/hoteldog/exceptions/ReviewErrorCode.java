package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    MIS_MATCH_USER_PK(HttpStatus.BAD_REQUEST,"로그인안 유저가 예약한 예약 pk가 아닙니다."),
    NOT_CHECK_OUT_STATUS(HttpStatus.BAD_REQUEST,"아직 체크아웃 한 예약이 아닙니다."),
    PAGE_COUNT_EXEEDED_ERROR(HttpStatus.BAD_REQUEST, "페이지 수 최대치를 초과하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
