package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.dto.board.BoardAddRequestDto;
import com.calendardev.calendardevelop.dto.board.BoardDetailResponseDto;
import com.calendardev.calendardevelop.dto.board.BoardResponseDto;
import com.calendardev.calendardevelop.dto.board.BoardUpdateRequestDto;
import com.calendardev.calendardevelop.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final LoginManager loginManager;

    @PostMapping("/create")
    public ResponseEntity<String> addOneBoard(@Valid @RequestBody BoardAddRequestDto requestDto,
                                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        boardService.addOneBoard(userId, requestDto);

        return new ResponseEntity<>("게시글이 생성되었습니다.",HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getPagedBoards(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        Page<BoardResponseDto> pagedBoardList = boardService.getPagedBoards(page-1, size);
        return new ResponseEntity<>(pagedBoardList.getContent(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardDetailResponseDto> getOneBoardDetail(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        BoardDetailResponseDto board = boardService.getOneBoardDetail(id, page-1, size);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable Long id,
                                            @Valid @RequestBody BoardUpdateRequestDto requestDto,
                                            HttpServletRequest httpServletRequest){
        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        boardService.updateBoard(id, userId, requestDto);

        return new ResponseEntity<>("게시글이 수정되었습니다.",HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id,
                                            HttpServletRequest httpServletRequest){
        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        boardService.deleteBoard(id, userId);

        return new ResponseEntity<>("게시글이 삭제되었습니다.",HttpStatus.OK);

    }

}
