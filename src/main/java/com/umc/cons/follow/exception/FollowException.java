package com.umc.cons.follow.exception;


public class FollowException extends RuntimeException{
    public FollowException() {
        super();
    }

    public FollowException(String message) {
        super(message);
    }

    public FollowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FollowException(Throwable cause) {
        super(cause);
    }

    protected FollowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
