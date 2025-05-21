package com.calendardev.calendardevelop.dto.exception;

public class ExceptionDto {
    private final String errorCode;
    private final String message;
    private final String location; // 추가됨

    public ExceptionDto(String errorCode, String message, String location) {
        this.errorCode = errorCode;
        this.message = message;
        this.location = location;
    }

    public ExceptionDto(String errorCode, String message) {

        this(errorCode, message, null);
    }
}
