package com.umc.cons.member.exception;

public class MemberDuplicatedException extends RuntimeException{
    public MemberDuplicatedException() {
    }

    public MemberDuplicatedException(String message) {
        super(message);
    }

    public MemberDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
