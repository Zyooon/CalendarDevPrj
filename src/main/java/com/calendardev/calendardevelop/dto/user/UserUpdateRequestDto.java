package com.calendardev.calendardevelop.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

    private final String username;

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private final String oldPassword;

    private final String newPassword;

    public UserUpdateRequestDto(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

}
