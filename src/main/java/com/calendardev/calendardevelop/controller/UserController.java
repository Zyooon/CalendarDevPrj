package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.common.Const;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> singUp(@RequestBody SignUpRequestDto requestDto){
        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoReponseDto> showOneUser(@PathVariable Long id){

        UserInfoReponseDto userInfoReponseDto = userService.showOneUser(id);

        return new ResponseEntity<>(userInfoReponseDto, HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateOneUser(@PathVariable Long id,
                                              @RequestBody UpdateUserRequestDto requestDto){

        userService.updateOneUser(id, requestDto);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOneUser(@PathVariable Long id,
                                              @RequestBody DeleteUserRequestDto requestDto){

        userService.deleteOneUser(id, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto requestDto,
                                                  HttpServletRequest httpServletRequest){

        if(httpServletRequest.getSession(false) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 로그인된 사용자입니다.");
        }

        LoginResponseDto loginResponseDto = userService.login(requestDto);

        HttpSession session = httpServletRequest.getSession();

        UserInfoReponseDto loginUser = userService.showOneUser(loginResponseDto.getId());

        session.setAttribute(Const.LOGIN_USER, loginUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();

            // postman 에서는 서버에서 자체적으로 쿠키(JSESSIONID)를 계속 보냄
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 즉시 만료
            response.addCookie(cookie);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
