package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.Const;
import com.calendardev.calendardevelop.dto.comment.CommnetAddRequestDto;
import com.calendardev.calendardevelop.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/calendar/board/{boardId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("comment")
    public ResponseEntity<Void> addComment(@PathVariable Long boardId,
                                           @RequestBody CommnetAddRequestDto requestDto,
                                           HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 먼저 해야합니다.");
        }

        Long userId = (Long)session.getAttribute(Const.USER_ID);

        commentService.addComment(boardId, userId, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
