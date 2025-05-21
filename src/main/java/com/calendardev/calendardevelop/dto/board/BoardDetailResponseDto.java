package com.calendardev.calendardevelop.dto.board;

import com.calendardev.calendardevelop.dto.comment.CommentResponseDto;
import com.calendardev.calendardevelop.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardDetailResponseDto {
    private final Long id;

    private final String title;

    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private final List<CommentResponseDto> comments;

    public BoardDetailResponseDto(Board board, List<CommentResponseDto> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.comments = comments;
    }
}
