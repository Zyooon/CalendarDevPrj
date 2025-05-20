package com.calendardev.calendardevelop.dto.board;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardUpdateRequestDto {
    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private final String title;

    @Size(max = 200, message = "제목은 200자 이내여야 합니다.")
    private final String Contents;

    public BoardUpdateRequestDto(String title, String contents) {
        this.title = title;
        Contents = contents;
    }
}
