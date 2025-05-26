package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.exception.CustomException;
import com.calendardev.calendardevelop.enums.ErrorCode;
import com.calendardev.calendardevelop.dto.board.*;
import com.calendardev.calendardevelop.dto.comment.CommentResponseDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public BoardCreateResponseDto createBoard(Long userId, BoardCreateRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Board board = new Board(requestDto.getTitle(), requestDto.getContents(), user);

        return new BoardCreateResponseDto(saveBoardByUserIdOrElseThrow(board));
    }

    private Long saveBoardByUserIdOrElseThrow(Board board) {
        try{
            return boardRepository.save(board).getId();
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.BAD_REQUEST_CONTENT);
        }
    }


    @Transactional(readOnly = true)
    public Page<BoardResponseDto> getPagedBoards(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return boardRepository.findAllWithCommentCount(pageable);
    }

    public BoardDetailResponseDto getOneBoardDetail(Long boardId, int page, int size) {

        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        List<CommentResponseDto> commentList = getPagedCommentsByBoardId(boardId, pageable);

        return new BoardDetailResponseDto(findBoard, commentList);
    }

    private List<CommentResponseDto> getPagedCommentsByBoardId(Long boardId, Pageable pageable) {
        Page<Comment> pagedCommentList = commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId, pageable);

        return pagedCommentList.stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    @Transactional
    public void updateBoard(Long id, Long userId, BoardUpdateRequestDto requestDto) {

        Board findBoard = boardRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        validateBoardOwner(findBoard, userId);

        updateTitleAndContents(findBoard, requestDto);
    }



    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        validateBoardOwner(findBoard, userId);

        deleteAllCommentListIfExist(boardId);

        boardRepository.delete(findBoard);
    }

    private void validateBoardOwner(Board findBoard, Long userId){
        if(!findBoard.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.POST_NOT_OWNED);
        }
    }

    private void updateTitleAndContents(Board findBoard, BoardUpdateRequestDto requestDto) {
        if(!Strings.isBlank(requestDto.getTitle())){
            findBoard.updateTitle(requestDto.getTitle());
        }

        if(!Strings.isBlank(requestDto.getContents())){
            findBoard.updateContents(requestDto.getContents());
        }
    }

    private void deleteAllCommentListIfExist(Long boardId){
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId).stream().toList();

        if(!commentList.isEmpty()){commentRepository.deleteAll(commentList);}
    }
}
