package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.common.ResponseMessege;
import com.calendardev.calendardevelop.dto.comment.CommentAddReponseDto;
import com.calendardev.calendardevelop.dto.comment.CommnetRequestDto;
import com.calendardev.calendardevelop.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final LoginManager loginManager;

    @PostMapping
    public ResponseEntity<CommentAddReponseDto> addComment(@PathVariable Long boardId,
                                           @Valid @RequestBody CommnetRequestDto requestDto,
                                           HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        CommentAddReponseDto reponseDto = commentService.addComment(boardId, userId, requestDto);

        return new ResponseEntity<>(reponseDto, HttpStatus.CREATED);

    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              @Valid @RequestBody CommnetRequestDto requestDto,
                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        commentService.updateComment(commentId, boardId, userId, requestDto);

        return new ResponseEntity<>(ResponseMessege.COMMENT_DELETED.getMessage(),HttpStatus.OK);

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        commentService.deleteComment(commentId, boardId, userId);

        return new ResponseEntity<>(ResponseMessege.COMMENT_DELETED.getMessage(),HttpStatus.OK);

    }
}
