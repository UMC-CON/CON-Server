package com.umc.cons.follow.exception;


public class AlreadyExistFollowException extends RuntimeException{
    public AlreadyExistFollowException() {
        super();
    }

    public AlreadyExistFollowException(String message) {
        super(message);
    }

    public AlreadyExistFollowException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistFollowException(Throwable cause) {
        super(cause);
    }

    protected AlreadyExistFollowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
