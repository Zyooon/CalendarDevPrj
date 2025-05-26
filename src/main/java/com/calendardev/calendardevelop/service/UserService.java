package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.PasswordManager;
import com.calendardev.calendardevelop.dto.user.*;
import com.calendardev.calendardevelop.entity.Board;
import com.calendardev.calendardevelop.entity.Comment;
import com.calendardev.calendardevelop.entity.User;
import com.calendardev.calendardevelop.enums.ErrorCode;
import com.calendardev.calendardevelop.exception.CustomException;
import com.calendardev.calendardevelop.repository.BoardRepository;
import com.calendardev.calendardevelop.repository.CommentRepository;
import com.calendardev.calendardevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        String encodedPassword = passwordManager.encodePassword(requestDto.getPassword());

        User user = requestDto.toEntity(encodedPassword);

        return new SignUpResponseDto(saveUserAndGetIdOrThrow(user));

    }

    private Long saveUserAndGetIdOrThrow(User user){
        try {
            return userRepository.save(user).getId();
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        passwordManager.validatePasswordMatchOrElseThrow(requestDto.getPassword(), user.getPassword());

        return new LoginResponseDto(user.getId());
    }

    public UserResponseDto getUserDetail(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDto(user);
    }

    @Transactional
    public void updateUser(Long id, UserUpdateRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        passwordManager.validatePasswordMatchOrElseThrow(requestDto.getOldPassword(), findUser.getPassword());

        updateUsername(findUser, requestDto);

        updateEncodedPassword(findUser, requestDto);
    }

    //유저 이름 업데이트
    private void updateUsername(User findUser, UserUpdateRequestDto requestDto) {
        if(Strings.isBlank(requestDto.getUsername())){
            findUser.updateUsername(requestDto.getUsername());
        }
    }

    //비밀번호 암호화 후 업데이트
    private void updateEncodedPassword(User findUser, UserUpdateRequestDto requestDto) {
        if(!Strings.isBlank(requestDto.getNewPassword())){
            String encodedNewPassword = passwordManager.encodePassword(requestDto.getNewPassword());
            findUser.updatePassword(encodedNewPassword);
        }
    }

    @Transactional
    public void deleteUser(Long id, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        passwordManager.validatePasswordMatchOrElseThrow(requestDto.getPassword(), findUser.getPassword());

        deleteAllCommentsByUserId(findUser);

        deleteAllBoardsByUserId(findUser);

        userRepository.delete(findUser);
    }

    private void deleteAllBoardsByUserId(User findUser) {
        List<Board> boardList = boardRepository.findAllByUserId(findUser.getId()).stream().toList();

        if(!boardList.isEmpty()){
            boardRepository.deleteAll(boardList);
        }
    }

    private void deleteAllCommentsByUserId(User findUser){
        List<Comment> commentList = commentRepository.findAllByUserId(findUser.getId()).stream().toList();

        if(!commentList.isEmpty()){
            commentRepository.deleteAll(commentList);
        }
    }
}
