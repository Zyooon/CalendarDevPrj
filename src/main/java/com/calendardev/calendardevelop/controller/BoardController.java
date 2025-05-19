package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.dto.board.BoardRequestDto;
import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<BoardResponseDto> createBoard(@PathVariable Long userId,
                                                        @RequestBody BoardRequestDto requestDto){
        BoardResponseDto responseDto = boardService.createBoard(userId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> showAllBoard(){
        List<BoardResponseDto> list = boardService.showAllBoard();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> showOneBoard(@PathVariable Long id){
        BoardResponseDto board = boardService.showOneBoard(id);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

//    @PatchMapping("update/{id}")
//    public ResponseEntity<Void> updateOneboard(@PathVariable Long id,
//                                               @RequestBody )

}
