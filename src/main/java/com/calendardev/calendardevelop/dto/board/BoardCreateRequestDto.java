package com.calendardev.calendardevelop.dto.board;

import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {
    private final Long userId;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 200, message = "내용은 200자 이내여야 합니다.")
    private final String contents;

    public BoardCreateRequestDto(Long userId, String title, String contents) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }

    public Board toEntity(User user) {
        return new Board(title, contents, user);
    }
}
