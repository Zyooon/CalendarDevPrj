package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.Const;
import com.calendardev.calendardevelop.common.LoginManager;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/calendar/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginManager loginManager;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto){
        userService.signUp(requestDto);

        return new ResponseEntity<>("회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto,
                                      HttpServletRequest httpServletRequest){

        if(httpServletRequest.getSession(false) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 로그인된 사용자입니다.");
        }

        LoginResponseDto loginResponseDto = userService.login(requestDto);

        HttpSession session = httpServletRequest.getSession();

        session.setAttribute(Const.USER_ID, loginResponseDto.getId());

        return new ResponseEntity<>("로그인 되었습니다.",HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response){
        HttpSession session = request.getSession(false);

        if(session == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 상태가 아닙니다.");
        }

        resetSessionAndCookies(session, response);

        return new ResponseEntity<>("로그아웃 되었습니다.",HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<UserResponseDto> getOneUserDetail(HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        UserResponseDto userResponseDto = userService.getOneUserDetail(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserUpdateRequestDto requestDto,
                                           HttpServletRequest httpServletRequest){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        userService.updateUser(userId, requestDto);
        
        return new ResponseEntity<>("유저 정보가 변경되었습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserDeleteRequestDto requestDto,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse){

        Long userId = loginManager.getUserIdOrElseNotLogin(httpServletRequest);

        userService.deleteUser(userId, requestDto);

        HttpSession session = httpServletRequest.getSession(false);

        if(session != null){
            resetSessionAndCookies(session, httpServletResponse);
        }

        return new ResponseEntity<>("탈퇴 처리되었습니다.",HttpStatus.OK);
    }



    private void resetSessionAndCookies(HttpSession session, HttpServletResponse response){
        //세션 만료
        session.invalidate();
        // postman 에서는 서버에서 자체적으로 쿠키(JSESSIONID)를 계속 보냄
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }

}
