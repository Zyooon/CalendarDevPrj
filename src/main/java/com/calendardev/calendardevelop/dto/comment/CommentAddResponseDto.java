package com.calendardev.calendardevelop.dto.comment;

import lombok.Getter;

@Getter
public class CommentAddResponseDto {
    private final Long id;

    public CommentAddResponseDto(Long id) {
        this.id = id;
    }
}
