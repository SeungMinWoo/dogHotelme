package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode{
    UNKNOWN_USER_PK(HttpStatus.BAD_REQUEST,"로그인 후 이용 해주세요."),
    RESERVATION_TABLE_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "예약 테이블 등록 실패"),
    RESERVATION_DOG_TABLE_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "예약 강아지 테이블 등록 실패"),
    ROOM_AND_DOG_RESERVATION_TABLE_REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "예약방&강아지 정보 예약 테이블 등록 실패"),
    HOTEL_ROOM_MANAGEMENT_TABLE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "호텔 방 관리 테이블 수정 실패"),
    NO_RESERVATION_INFORMATION(HttpStatus.BAD_REQUEST, "예약 정보 없음"),
    ROOM_PK_SELECT_FAILURE(HttpStatus.BAD_REQUEST, "방pk 셀렉트 실패"),
    FAILED_TO_DELETE_HOTEL_ROOM_DOG_TABLE(HttpStatus.BAD_REQUEST, "호텔방 강아지 테이블 삭제 실패"),
    FAILED_TO_DELETE_RESERVED_DOG_TABLE(HttpStatus.BAD_REQUEST, "예약 강아지 테이블 삭제 실패"),
    FAILED_TO_DELETE_HOTEL_RESERVATION_TABLE(HttpStatus.BAD_REQUEST, "호텔 예약 테이블 삭제 실패"),
    NO_ROOMS_AVAILABLE_FOR_THIS_DATE(HttpStatus.BAD_REQUEST, "해당 날짜에 방을 예약 할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
