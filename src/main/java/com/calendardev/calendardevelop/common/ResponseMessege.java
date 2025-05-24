package com.calendardev.calendardevelop.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessege {

    BOARD_UPDATED("게시글이 수정되었습니다."),
    BOARD_DELETED("게시글이 삭제되었습니다."),
    COMMENT_UPDATED("댓글이 수정되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    LOGIN_SUCCESS("로그인 되었습니다."),
    LOGOUT_SUCCESS("로그아웃 되었습니다."),
    USER_INFO_UPDATED("유저 정보가 변경되었습니다."),
    USER_DELETED("탈퇴 처리되었습니다.");

    private final String message;
}
