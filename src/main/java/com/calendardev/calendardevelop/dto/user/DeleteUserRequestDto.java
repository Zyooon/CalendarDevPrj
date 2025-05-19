package com.calendardev.calendardevelop.dto.user;

import lombok.Getter;

@Getter
public class DeleteUserRequestDto {
    private final String password;

    public DeleteUserRequestDto(String password) {
        this.password = password;
    }
}
