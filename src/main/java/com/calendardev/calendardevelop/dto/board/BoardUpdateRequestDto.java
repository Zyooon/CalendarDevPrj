package com.calendardev.calendardevelop.dto.board;

import lombok.Getter;

@Getter
public class BoardUpdateRequestDto {
    private final String title;
    private final String Contents;

    public BoardUpdateRequestDto(String title, String contents) {
        this.title = title;
        Contents = contents;
    }
}
