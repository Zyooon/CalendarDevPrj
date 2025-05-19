package com.calendardev.calendardevelop.controller;

import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){

        LoginResponseDto loginResponseDto = userService.login(requestDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

}
