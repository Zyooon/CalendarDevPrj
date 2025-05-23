package com.calendardev.calendardevelop.common;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordManager {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public void validatePasswordMatchOrElseThrow(String inputPassword, String encodedPassword){
        if(!passwordEncoder.matches(inputPassword, encodedPassword)){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

}
