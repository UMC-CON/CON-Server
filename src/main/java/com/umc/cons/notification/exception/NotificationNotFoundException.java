package com.umc.cons.notification.exception;

public class NotificationNotFoundException extends RuntimeException{
	public NotificationNotFoundException() {
	}

	public NotificationNotFoundException(String message) {
		super(message);
	}

	public NotificationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
