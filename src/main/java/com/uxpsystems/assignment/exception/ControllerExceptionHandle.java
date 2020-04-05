package com.uxpsystems.assignment.exception;

import com.uxpsystems.assignment.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class ControllerExceptionHandle {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Message> notFound(HttpServletRequest request, Exception e){
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<Message> notValid(HttpServletRequest request, Exception e){
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    @ExceptionHandler(RequestNotAllowedException.class)
    public ResponseEntity<Message> conflict(HttpServletRequest request, Exception e){
        Message message = new Message(e.getMessage());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.CONFLICT);
        return responseEntity;
    }
}
