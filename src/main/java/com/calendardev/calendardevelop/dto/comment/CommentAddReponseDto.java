package com.calendardev.calendardevelop.dto.comment;

import lombok.Getter;

@Getter
public class CommentAddReponseDto {
    private final Long id;

    public CommentAddReponseDto(Long id) {
        this.id = id;
    }
}
