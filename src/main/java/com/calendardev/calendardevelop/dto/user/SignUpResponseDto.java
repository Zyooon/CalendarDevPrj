package com.calendardev.calendardevelop.dto.user;

import lombok.Getter;

@Getter
public class SignUpResponseDto {
    private final Long id;

    public SignUpResponseDto(Long id) {
        this.id = id;
    }
}
