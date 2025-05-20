package com.calendardev.calendardevelop.service;

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

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword());

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());

        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다.");
        }

        if(!requestDto.getPassword().equals(user.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다.");
        }

        return new LoginResponseDto(user.get().getId());
    }

    public UserInfoReponseDto showOneUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."));

        return new UserInfoReponseDto(user);
    }

    @Transactional
    public void updateOneUser(Long id, UpdateUserRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        if(!findUser.getPassword().equals(requestDto.getOldPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        if(requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()){
            findUser.updateUsername(requestDto.getUsername());
        }

        if(requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()){
            findUser.updatePassword(requestDto.getNewPassword());
        }

    }

    public void deleteOneUser(Long id, DeleteUserRequestDto requestDto) {

        Optional<User> findUser = userRepository.findById(id);

        if(!findUser.get().getPassword().equals(requestDto.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(findUser.get());

    }
}
