package com.calendardev.calendardevelop.common;

import com.calendardev.calendardevelop.dto.exception.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllException(Exception e) {
        String location = getExceptionOccurredLocation(e);
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), location);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDto);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> handleCustomException(CustomException e){
        String location = getExceptionOccurredLocation(e);
        ExceptionDto exceptionDto = new ExceptionDto(e.getStatus(), e.getMessage(), location);
        return ResponseEntity.status(e.getStatus()).body(exceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST, errorMessage, getExceptionOccurredLocation(e));
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleResponseStatusException(ResponseStatusException e) {
        String location = getExceptionOccurredLocation(e);
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDto exceptionDto = new ExceptionDto(status, e.getReason(), location);
        return ResponseEntity.status(status).body(exceptionDto);
    }

    private String getExceptionOccurredLocation(Throwable e) {
        if (e == null || e.getStackTrace().length == 0) return "Unknown Location";
        StackTraceElement element = e.getStackTrace()[0];
        String className = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
        return className + "." + element.getMethodName() + " :: Line " + element.getLineNumber();
    }
}
