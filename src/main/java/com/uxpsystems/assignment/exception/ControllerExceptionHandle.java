package com.uxpsystems.assignment.exception;

import com.uxpsystems.assignment.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandle {

    private final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandle.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Message> notFound(Exception e){
        LOGGER.error("Resource Not Found:: {} ", e.getMessage());
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<Message> notValid(Exception e){
        LOGGER.error("Bad Request:: {} ", e.getMessage());
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler(RequestNotAllowedException.class)
    public ResponseEntity<Message> conflict(Exception e){
        LOGGER.error("Duplicate Request:: {} ", e.getMessage());
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.CONFLICT);
        return responseEntity;
    }
}
