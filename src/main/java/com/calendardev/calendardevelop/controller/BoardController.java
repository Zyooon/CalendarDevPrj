package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.enums.ResponseMessege;
import com.calendardev.calendardevelop.dto.board.*;
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
@RequestMapping("/calendar/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final LoginManager loginManager;

    @PostMapping("/write")
    public ResponseEntity<BoardCreateResponseDto> createBoard(@Valid @RequestBody BoardCreateRequestDto requestDto,
                                                              HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        BoardCreateResponseDto responseDto = boardService.createBoard(userId, requestDto);

        return new ResponseEntity<>(responseDto ,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getPagedBoards(@RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        //인덱스 0번이 1페이지이지만, 클라이언트에서 요청보낼때 혼란을 방지하기 위해서 Default 값에서 -1로 연산.
        Page<BoardResponseDto> pagedBoardList = boardService.getPagedBoards(page-1, size);
        return new ResponseEntity<>(pagedBoardList.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> getOneBoardDetail(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        //인덱스 0번이 1페이지이지만, 클라이언트에서 요청보낼때 혼란을 방지하기 위해서 Default 값에서 -1로 연산.
        BoardDetailResponseDto board = boardService.getOneBoardDetail(id, page-1, size);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable Long id,
                                                       @Valid @RequestBody BoardUpdateRequestDto requestDto,
                                                       HttpServletRequest httpServletRequest){
        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        boardService.updateBoard(id, userId, requestDto);

        return new ResponseEntity<>(ResponseMessege.BOARD_UPDATED.getMessage(),HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id,
                                            HttpServletRequest httpServletRequest){
        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        boardService.deleteBoard(id, userId);

        return new ResponseEntity<>(ResponseMessege.BOARD_DELETED.getMessage(),HttpStatus.OK);

    }

}
