package com.calendardev.calendardevelop.dto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionDto {
    private final int status;
    private final String message;
    private final String location;

    public ExceptionDto(HttpStatus status, String message, String location) {
        this.status = status.value();
        this.message = message;
        this.location = location;
    }
}
