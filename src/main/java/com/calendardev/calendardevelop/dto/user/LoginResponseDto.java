package com.calendardev.calendardevelop.dto.user;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final Long id;

    public LoginResponseDto(Long id) {
        this.id = id;
    }
}
