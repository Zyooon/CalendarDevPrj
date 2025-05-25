package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.common.ResponseMessege;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginManager loginManager;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto){

        SignUpResponseDto responseDto = userService.signUp(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto,
                                      HttpServletRequest httpServletRequest){

        LoginResponseDto loginResponseDto = userService.login(requestDto);

        loginManager.setUserIdToSession(httpServletRequest, loginResponseDto.getId());

        return new ResponseEntity<>(ResponseMessege.LOGIN_SUCCESS.getMessage() ,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response){

        loginManager.resetSessionAndCookies(request, response);

        return new ResponseEntity<>(ResponseMessege.LOGOUT_SUCCESS.getMessage(),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUserDetail(HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        UserResponseDto userResponseDto = userService.getUserDetail(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserUpdateRequestDto requestDto,
                                           HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        userService.updateUser(userId, requestDto);
        
        return new ResponseEntity<>(ResponseMessege.USER_INFO_UPDATED.getMessage(),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserDeleteRequestDto requestDto,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse){

        Long userId = loginManager.getUserIdFromSession(httpServletRequest);

        userService.deleteUser(userId, requestDto);

        loginManager.resetSessionAndCookies(httpServletRequest, httpServletResponse);

        return new ResponseEntity<>(ResponseMessege.USER_DELETED.getMessage(),HttpStatus.OK);
    }

}
