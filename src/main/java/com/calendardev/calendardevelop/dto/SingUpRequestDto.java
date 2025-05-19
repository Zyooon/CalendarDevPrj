package com.calendardev.calendardevelop.dto;

import lombok.Getter;

@Getter
public class SingUpRequestDto {
    private final String username;

    private final String email;

    private final String password;

    public SingUpRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
