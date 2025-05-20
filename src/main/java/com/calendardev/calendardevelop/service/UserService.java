package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.PasswordManager;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        String encodedPassword = passwordManager.encodePassword(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), encodedPassword);

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());

        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다.");
        }

        if(!passwordManager.matchPassword(requestDto.getPassword(), user.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponseDto(user.get().getId());
    }

    public UserResponseDto showOneUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다."));

        return new UserResponseDto(user);
    }

    @Transactional
    public void updateOneUser(Long id, UserUpdateRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        if(passwordManager.matchPassword(requestDto.getOldPassword(),findUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if(requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()){
            findUser.updateUsername(requestDto.getUsername());
        }

        if(requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()){
            String encodedNewPassword = passwordManager.encodePassword(requestDto.getNewPassword());
            findUser.updatePassword(encodedNewPassword);
        }

    }

    public void deleteOneUser(Long id, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        if(passwordManager.matchPassword(requestDto.getPassword(), findUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(findUser);
    }

}
