package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.CustomException;
import com.calendardev.calendardevelop.common.ErrorCode;
import com.calendardev.calendardevelop.dto.comment.CommnetRequestDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addComment(Long boardId, Long userId, CommnetRequestDto requestDto) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(requestDto.getContents());

        comment.setUserAndBoard(findUser, findBoard);

        commentRepository.save(comment);

    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, Long userId, CommnetRequestDto requestDto) {
        Optional<Comment> findComment = commentRepository.findByIdAndBoardId(commentId, boardId);

        if(findComment.isEmpty()){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(!findComment.get().getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.POST_NOT_OWNED);
        }

        if(isBlank(requestDto.getContents())){
            findComment.get().updateContents(requestDto.getContents());
        }

    }
    public void deleteComment(Long commentId, Long boardId, Long userId) {
        Optional<Comment> findComment = commentRepository.findByIdAndBoardId(commentId, boardId);

        if(findComment.isEmpty()){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(!findComment.get().getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.COMMENT_NOT_OWNED);
        }

        commentRepository.delete(findComment.get());

    }
    private boolean isBlank(String str){

        return str != null && !str.trim().isEmpty();
    }


}
