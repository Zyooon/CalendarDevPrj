package com.calendardev.calendardevelop.dto.comment;

import lombok.Getter;

@Getter
public class CommentCreateResponseDto {
    private final Long id;

    public CommentCreateResponseDto(Long id) {
        this.id = id;
    }
}
