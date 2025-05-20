package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.Const;
import com.calendardev.calendardevelop.dto.board.BoardAddRequestDto;
import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.dto.board.BoardUpdateRequestDto;
import com.calendardev.calendardevelop.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/calendar/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardAddRequestDto requestDto,
                                                        HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);

        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 먼저 해야합니다.");
        }

        Long userId = (Long)session.getAttribute(Const.USER_ID);

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

    @PatchMapping("update/{id}")
    public ResponseEntity<Void> updateboard(@PathVariable Long id,
                                            @Valid @RequestBody BoardUpdateRequestDto requestDto,
                                            HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 먼저 해야합니다.");
        }
        Long userId = (Long)session.getAttribute(Const.USER_ID);

        boardService.updateBoard(id, userId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id,
                                            HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession(false);
        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 먼저 해야합니다.");
        }
        Long userId = (Long)session.getAttribute(Const.USER_ID);

        boardService.deleteBoard(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
