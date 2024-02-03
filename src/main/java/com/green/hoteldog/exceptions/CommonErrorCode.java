package com.green.hoteldog.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    ACCEPTED
            (HttpStatus.ACCEPTED,"처리가 완료되지 않음"),
    NON_AUTHORITATIVE_INFORMATION
            (HttpStatus.NON_AUTHORITATIVE_INFORMATION,"신뢰 할 수 없는 정보"),
    NO_CONTENT
            (HttpStatus.NO_CONTENT,"응답 콘텐츠 없음"),
    RESET_CONTENT
            (HttpStatus.RESET_CONTENT,"브라우저 화면 리셋 필요"),
    PARTIAL_CONTENT
            (HttpStatus.PARTIAL_CONTENT,"일부 콘텐츠만 응답"),
    MULTI_STATUS
            (HttpStatus.MULTI_STATUS,"처리 결과 상태 여러개"),
    MOVED_PERMANENTLY
            (HttpStatus.MOVED_PERMANENTLY,"지정한 리소스가 새로운 URI로 이동"),
    FOUND
            (HttpStatus.FOUND,"요청한 리소스슬 다른 URI에서 찾음"),
    SEE_OTHER
            (HttpStatus.SEE_OTHER,"다른 위치로 요청하라"),
    TEMPORARY_REDIRECT
            (HttpStatus.TEMPORARY_REDIRECT,"임시로 리다이렉션이 필요"),


    //클라이언트 에러
    INVALID_PARAMETER
            (HttpStatus.BAD_REQUEST,"요청 구문 잘못됨"),
    UNAUTHORIZED
            (HttpStatus.UNAUTHORIZED,"액세스 권한 없음"),
    FORBIDDEN
            (HttpStatus.FORBIDDEN,"접근 금지됨"),
    RESOURCE_NOT_FOUND
            (HttpStatus.NOT_FOUND,"리소스 찾을 수 없음"),
    TIME_OUT
            (HttpStatus.REQUEST_TIMEOUT,"서버 타임아웃"),
    CONFLICT
            (HttpStatus.CONFLICT,"서버가 요청을 수행하는 중 충돌 발생"),
    REQUEST_ENTITY_TOO_LARGE
            (HttpStatus.REQUEST_ENTITY_TOO_LARGE,"요청 메세지 너무 큼"),
    REQUEST_URI_TOO_LONG
            (HttpStatus.REQUEST_URI_TOO_LONG,"요청 URI 너무 김"),
    REQUEST_HEADER_FIELDS_TOO_LARGE
            (HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,"헤더의 길이가 너무 김"),



    //서버 에러
    INTERNAL_SERVER_ERROR
            (HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류"),
    NOT_IMPLEMENTED
            (HttpStatus.NOT_IMPLEMENTED,"구현되지 않음"),
    BAD_GATEWAY
            (HttpStatus.BAD_GATEWAY,"서버가 잘못된 응답 받음"),
    SERVICE_UNAVAILABLE
            (HttpStatus.SERVICE_UNAVAILABLE,"서비스 제공 불가"),
    GATEWAY_TIMEOUT
            (HttpStatus.GATEWAY_TIMEOUT,"서버 타임아웃 발생"),
    HTTP_VERSION_NOT_SUPPORTED
            (HttpStatus.HTTP_VERSION_NOT_SUPPORTED,"요청 HTTP 버전 지원하지 않음"),
    INSUFFICIENT_STORAGE
            (HttpStatus.INSUFFICIENT_STORAGE,"서버 용량 부족");


    private final HttpStatus httpStatus;
    private final String message;
}
