package com.calendardev.calendardevelop.dto.comment;

import lombok.Getter;
import com.calendardev.calendardevelop.entity.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    
    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public CommentResponseDto(Long id, String contents, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.contents = contents;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
