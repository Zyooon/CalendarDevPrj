package com.calendardev.calendardevelop.dto.user;

import com.calendardev.calendardevelop.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long id;

    private final String username;

    private final String email;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }


}
