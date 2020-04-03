package com.uxpsystems.assignment.exception;

public class RequestNotValidException extends RuntimeException {
    public RequestNotValidException() {
        super();
    }

    public RequestNotValidException(String message) {
        super(message);
    }
}
