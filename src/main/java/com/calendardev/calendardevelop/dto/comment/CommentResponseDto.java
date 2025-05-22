package com.calendardev.calendardevelop.dto.comment;

import lombok.Getter;
import com.calendardev.calendardevelop.entity.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;

    private final Long userId;
    
    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
