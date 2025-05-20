package com.calendardev.calendardevelop.common;

import com.calendardev.calendardevelop.dto.exception.ExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDto> handleCustomException(CustomException e){
        String location = extractLocation(e);
        ExceptionDto exceptionDto = new ExceptionDto(e.getErrorCode(), e.getMessage(), location);
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    private String extractLocation(Throwable e) {
        if (e == null || e.getStackTrace().length == 0) return "Unknown Location";
        StackTraceElement element = e.getStackTrace()[0];
        return element.getMethodName() + ":" + element.getLineNumber();
    }

}
