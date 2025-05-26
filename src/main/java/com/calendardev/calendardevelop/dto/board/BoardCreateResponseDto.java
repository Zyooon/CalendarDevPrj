package com.calendardev.calendardevelop.dto.board;

import lombok.Getter;

@Getter
public class BoardCreateResponseDto {
    private final Long id;

    public BoardCreateResponseDto(Long id) {
        this.id = id;
    }
}
