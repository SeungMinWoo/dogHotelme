package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum HotelErrorCode implements ErrorCode{
    NON_EXIST_HOTEL_PK(HttpStatus.NOT_FOUND,"존재하지 않는 호텔 PK"),
    NON_EXIST_ROOM_DATE(HttpStatus.NOT_FOUND,"호텔 방 날짜 데이터 존재하지 않음"),
    NON_EXIST_PAGE_DATA(HttpStatus.NOT_FOUND,"존재하지 않는 페이지 번호"),
    UNKNOWN_DATE_FORM(HttpStatus.BAD_REQUEST,"날짜를 형식에 맞게 입력 해주세요.");
    private final HttpStatus httpStatus;
    private final String message;
}
