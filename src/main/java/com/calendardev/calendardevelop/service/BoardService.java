package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.dto.board.BoardRequestDto;
import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponseDto createBoard(Long userId, BoardRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Board board = new Board(requestDto.getTitle(), requestDto.getContents());
        
        board.setUser(user);

        Board savedBoard = boardRepository.save(board);


        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> showAllBoard() {
        return boardRepository.findAll()
                .stream()
                .map(BoardResponseDto::toDto)
                .toList();
    }

    public BoardResponseDto showOneBoard(Long id) {
        Board findBoard = boardRepository.findById(id).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."));
        return new BoardResponseDto(findBoard);
    }
}
