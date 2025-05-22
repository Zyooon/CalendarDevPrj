package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.CustomException;
import com.calendardev.calendardevelop.dto.board.BoardAddRequestDto;
import com.calendardev.calendardevelop.dto.board.BoardDetailResponseDto;
import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.dto.board.BoardUpdateRequestDto;
import com.calendardev.calendardevelop.dto.comment.CommentResponseDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public void addOneBoard(Long userId, BoardAddRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        Board board = new Board(requestDto.getTitle(), requestDto.getContents(), user);

        try{
            boardRepository.save(board);
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(HttpStatus.CONFLICT, "잘못된 내용이 전달되었습니다.");
        }

    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> getPagedBoards(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Board> pagedBoardList = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

        if(pagedBoardList.isEmpty()){
            throw new CustomException(HttpStatus.NO_CONTENT, "게시글이 존재하지 않습니다.");
        }

        return pagedBoardList.map(BoardResponseDto::new);
    }

    public BoardDetailResponseDto getOneBoardDetail(Long boardId, int page, int size) {

        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> pagedCommentList = commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId, pageable);

        List<CommentResponseDto> commentList = pagedCommentList.stream()
                .map(CommentResponseDto::new)
                .toList();

        return new BoardDetailResponseDto(findBoard, commentList);
    }

    @Transactional
    public void updateBoard(Long id, Long userId, BoardUpdateRequestDto requestDto) {

        Board findBoard = boardRepository.findById(id)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        if(!findBoard.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "해당 사용자의 일정이 아닙니다.");
        }

        if(isBlank(requestDto.getTitle())){
            findBoard.updateTitle(requestDto.getTitle());
        }

        if(isBlank(requestDto.getContents())){
            findBoard.updateContents(requestDto.getContents());
        }
    }

    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));

        if(!findBoard.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.FORBIDDEN, "본인의 게시글만 삭제할 수 있습니다.");
        }

        List<Comment> commentList = commentRepository.findAllByBoardId(boardId).stream().toList();

        if(!commentList.isEmpty()){
            commentRepository.deleteAll(commentList);
        }

        boardRepository.delete(findBoard);
    }

    private boolean isBlank(String str){

        return str != null && !str.trim().isEmpty();
    }
}
