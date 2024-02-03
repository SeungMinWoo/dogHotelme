package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BAD_REQUEST_BOARD_PK(HttpStatus.BAD_REQUEST,"잘못된 게시판pk값이 입력되었습니다."),
    BAD_REQUEST_COMMENT_PK(HttpStatus.BAD_REQUEST,"잘못된 댓글pk값이 입력되었습니다."),
    SEARCH_LENGTH_ERROR(HttpStatus.BAD_REQUEST,"검색은 최소 2글자 이상 검색해야 합니다."),
    PICS_SIZE_OVER(HttpStatus.BAD_REQUEST,"사진은 최대 3장까지만 등록됩니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
