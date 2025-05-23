package com.calendardev.calendardevelop.dto.board;

import lombok.Getter;

@Getter
public class BoardAddResponseDto {
    private final Long id;

    public BoardAddResponseDto(Long id) {
        this.id = id;
    }
}
