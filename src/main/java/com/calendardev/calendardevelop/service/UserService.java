package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.dto.SignUpRequestDto;
import com.calendardev.calendardevelop.dto.SignUpResponseDto;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPassword());

        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
