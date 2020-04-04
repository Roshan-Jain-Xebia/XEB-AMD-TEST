package com.uxpsystems.assignment.exception;

public class RequestNotAllowedException extends RuntimeException {
    public RequestNotAllowedException() {
        super();
    }

    public RequestNotAllowedException(String message) {
        super(message);
    }
}
