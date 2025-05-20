package com.calendardev.calendardevelop.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommnetRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 100, message = "내용은 100자 이내여야 합니다.")
    private final String contents;

    public CommnetRequestDto(String contents) {
        this.contents = contents;
    }
}
