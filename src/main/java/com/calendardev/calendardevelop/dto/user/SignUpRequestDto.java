package com.calendardev.calendardevelop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "이름은 특수문자를 제외한 2~10자리여야 합니다.")
    @NotBlank(message = "이름은 필수 입력사항입니다.")
    private final String username;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력사항입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
    private final String password;

    public SignUpRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
