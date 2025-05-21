package com.calendardev.calendardevelop.common;

import com.calendardev.calendardevelop.dto.exception.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllException(Exception e) {
        String location = extractLocation(e);
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage(), location);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDto);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> handleCustomException(CustomException e){
        String location = extractLocation(e);
        ExceptionDto exceptionDto = new ExceptionDto(e.getStatus(), e.getMessage(), location);
        return ResponseEntity.status(e.getStatus()).body(exceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleResponseStatusException(ResponseStatusException e) {
        String location = extractLocation(e);
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionDto dto = new ExceptionDto(status, e.getReason(), location);
        return ResponseEntity.status(status).body(dto);
    }

    private String extractLocation(Throwable e) {
        if (e == null || e.getStackTrace().length == 0) return "Unknown Location";
        StackTraceElement element = e.getStackTrace()[0];
        String className = element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
        return className + " - " + element.getMethodName() + " - " + element.getLineNumber();
    }

}
