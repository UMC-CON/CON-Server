package com.umc.cons.common.jwt.exception;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException() {
    }

    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
