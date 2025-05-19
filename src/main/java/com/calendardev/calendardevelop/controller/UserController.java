package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.dto.LoginRequestDto;
import com.calendardev.calendardevelop.dto.LoginResponseDto;
import com.calendardev.calendardevelop.dto.SignUpRequestDto;
import com.calendardev.calendardevelop.dto.SignUpResponseDto;
import com.calendardev.calendardevelop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> singUp(@RequestBody SignUpRequestDto requestDto){
        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){

        LoginResponseDto loginResponseDto = userService.login(requestDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
