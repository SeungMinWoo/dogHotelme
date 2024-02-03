package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthorizedErrorCode implements ErrorCode{
    NOT_EXISTS_REFRESH_TOKEN(HttpStatus.NOT_FOUND,"토큰이 없습니다."),
    REFRESH_TOKEN_IS_EXPIRATION(HttpStatus.NOT_FOUND,"토큰 기간이 만료 되었습니다."),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED,"로그인 후 이용 가능합니다");

    private final HttpStatus httpStatus;
    private final String message;
}
