package com.calendardev.calendardevelop.dto.board;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private final Long userId;

    private final String title;

    private final String contents;

    public BoardRequestDto(Long userId, String title, String contents) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
