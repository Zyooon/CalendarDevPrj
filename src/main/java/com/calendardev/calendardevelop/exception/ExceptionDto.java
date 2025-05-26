package com.calendardev.calendardevelop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionDto {
    //에러코드로 관리
    private final int status;
    
    //에러메세지
    private final String message;
    
    //에러가 발생한 지점 - 클래스, 메서드, 라인번호
    private final String location;

    public ExceptionDto(HttpStatus status, String message, String location) {
        this.status = status.value();
        this.message = message;
        this.location = location;
    }
}
