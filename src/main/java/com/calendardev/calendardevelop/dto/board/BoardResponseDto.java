package com.calendardev.calendardevelop.dto.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private final Long id;

    private final String username;

    private final String title;

    private final String contents;

    private final long commentCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public BoardResponseDto(Long id, String username, String title, String contents, long commentCount, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
