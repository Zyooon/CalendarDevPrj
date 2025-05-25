package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.exception.CustomException;
import com.calendardev.calendardevelop.enums.ErrorCode;
import com.calendardev.calendardevelop.dto.comment.CommentAddReponseDto;
import com.calendardev.calendardevelop.dto.comment.CommnetRequestDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentAddReponseDto addComment(Long boardId, Long userId, CommnetRequestDto requestDto) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(requestDto.getContents(), findUser, findBoard);

        Long commentId = commentRepository.save(comment).getId();

        return new CommentAddReponseDto(commentId);
    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, Long userId, CommnetRequestDto requestDto) {
        Comment findComment = commentRepository.findByIdAndBoardId(commentId, boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));;

        validateCommentOwner(findComment, userId);

        saveCommentIfIsNotBlank(findComment, requestDto);
    }

    private void saveCommentIfIsNotBlank(Comment findComment, CommnetRequestDto requestDto) {
        if(!Strings.isBlank(requestDto.getContents())){
            findComment.updateContents(requestDto.getContents());
        }
    }

    public void deleteComment(Long commentId, Long boardId, Long userId) {
        Comment findComment = commentRepository.findByIdAndBoardId(commentId, boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        validateCommentOwner(findComment, userId);

        commentRepository.delete(findComment);

    }
    //String null 체크
    private boolean isBlank(String str){
        return str != null && !str.trim().isEmpty();
    }

    private void validateCommentOwner(Comment findComment, Long userId){
        if(!findComment.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.COMMENT_NOT_OWNED);
        }
    }
}
