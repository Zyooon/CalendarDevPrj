package com.calendardev.calendardevelop.dto.user;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    private final String username;

    private final String oldPassword;

    private final String newPassword;

    public UpdateUserRequestDto(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

}
