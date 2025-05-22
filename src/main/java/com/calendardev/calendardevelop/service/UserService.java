package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.CustomException;
import com.calendardev.calendardevelop.common.ErrorCode;
import com.calendardev.calendardevelop.common.PasswordManager;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {

        String encodedPassword = passwordManager.encodePassword(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), encodedPassword);

        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordManager.isPasswordMatch(requestDto.getPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return new LoginResponseDto(user.getId());
    }

    public UserResponseDto getOneUserDetail(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDto(user);
    }

    @Transactional
    public void updateUser(Long id, UserUpdateRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordManager.isPasswordMatch(requestDto.getOldPassword(),findUser.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if(requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()){
            findUser.updateUsername(requestDto.getUsername());
        }

        if(requestDto.getNewPassword() != null && !requestDto.getNewPassword().isEmpty()){
            String encodedNewPassword = passwordManager.encodePassword(requestDto.getNewPassword());
            findUser.updatePassword(encodedNewPassword);
        }

    }

    @Transactional
    public void deleteUser(Long id, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordManager.isPasswordMatch(requestDto.getPassword(), findUser.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        List<Comment> commentList = commentRepository.findAllByUserId(findUser.getId()).stream().toList();

        if(!commentList.isEmpty()){
            commentRepository.deleteAll(commentList);
        }

        List<Board> boardList = boardRepository.findAllByUserId(findUser.getId()).stream().toList();

        if(!boardList.isEmpty()){
            boardRepository.deleteAll(boardList);
        }

        userRepository.delete(findUser);
    }

}
