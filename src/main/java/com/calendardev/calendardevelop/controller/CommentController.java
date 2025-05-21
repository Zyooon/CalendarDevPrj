package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.dto.comment.CommnetRequestDto;
import com.calendardev.calendardevelop.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar/board/{boardId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final LoginManager loginManager;

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@PathVariable Long boardId,
                                           @RequestBody CommnetRequestDto requestDto,
                                           HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        commentService.addComment(boardId, userId, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              @RequestBody CommnetRequestDto requestDto,
                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        commentService.updateComment(commentId, boardId, userId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        commentService.deleteComment(commentId, boardId, userId);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
