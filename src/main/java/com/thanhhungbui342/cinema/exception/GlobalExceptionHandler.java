package com.thanhhungbui342.cinema.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleVadilationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(
            error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ErrorResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Request is undefined!")
                            .errors(errors)
                            .timestamp(LocalDateTime.now())
                            .build();
    }                       

    @ExceptionHandler(AppException.class) 
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMovieNotFoundException(AppException exception){
        return ErrorResponse.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message(exception.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception exception){
        log.error("Unhandle Exception appears");

        return ErrorResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Bug in System, Try it again")
                            .timestamp(LocalDateTime.now())
                            .build();
    }


}
