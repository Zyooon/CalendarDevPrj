package com.calendardev.calendardevelop.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    public UserDeleteRequestDto(String password) {
        this.password = password;
    }
}
