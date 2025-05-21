package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.dto.comment.CommnetRequestDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        Comment comment = new Comment(requestDto.getContents());

        comment.setUserAndBoard(findUser, findBoard);

        commentRepository.save(comment);

    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, Long userId, CommnetRequestDto requestDto) {
        Optional<Comment> findComment = commentRepository.findByIdAndBoardId(commentId, boardId);

        if(findComment.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");
        }

        if(!findComment.get().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 사용자의 일정이 아닙니다.");
        }

        if(isBlank(requestDto.getContents())){
            findComment.get().updateContents(requestDto.getContents());
        }

    }
    public void deleteComment(Long commentId, Long boardId, Long userId) {
        Optional<Comment> findComment = commentRepository.findByIdAndBoardId(commentId, boardId);

        if(findComment.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");
        }

        if(!findComment.get().getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인의 게시글만 삭제할 수 있습니다.");
        }


        commentRepository.delete(findComment.get());

    }
    private boolean isBlank(String str){

        return str != null && !str.trim().isEmpty();
    }


}
