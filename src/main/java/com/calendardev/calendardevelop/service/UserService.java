package com.calendardev.calendardevelop.service;

import com.calendardev.calendardevelop.common.CustomException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void signUp(SignUpRequestDto requestDto) {

        try {
            String encodedPassword = passwordManager.encodePassword(requestDto.getPassword());

            User user = new User(requestDto.getUsername(), requestDto.getEmail(), encodedPassword);

            userRepository.save(user);

        }catch (DataIntegrityViolationException e) {

            throw new CustomException(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다.");

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중 오류가 발생했습니다.");

        }
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());

        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다.");
        }

        if(!passwordManager.isPasswordMatch(requestDto.getPassword(), user.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponseDto(user.get().getId());
    }

    public UserResponseDto getOneUserDetail(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 회원 정보입니다."));

        return new UserResponseDto(user);
    }

    @Transactional
    public void updateUser(Long id, UserUpdateRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        if(!passwordManager.isPasswordMatch(requestDto.getOldPassword(),findUser.getPassword())){
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

    public void deleteUser(Long id, UserDeleteRequestDto requestDto) {

        User findUser = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 정보가 없습니다."));

        if(!passwordManager.isPasswordMatch(requestDto.getPassword(), findUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
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
