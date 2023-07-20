package com.umc.cons.common.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//에러 코드 관리
@Getter
public enum BaseResponseStatus {

    //200: 요청 성공
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공했습니다."),

    //400: Request 오류, Response 오류
    REQUEST_ERROR(false, HttpStatus.BAD_REQUEST.value(), "입력값을 확인해주세요."),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),

    //50 : Database, Server 오류
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
